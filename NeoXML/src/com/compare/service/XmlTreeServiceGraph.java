package com.compare.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

import com.compare.parse.component.HierarchyIdentifier;
import com.compare.parse.component.XmlElement;
import com.compare.persist.neo4j.Neo4jDatabaseHandler;
import com.compare.xml.XmlElements;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XmlTreeServiceGraph implements XmlTreeService {

	private final static Logger LOGGER = Logger
			.getLogger(XmlTreeServiceGraph.class.getName());

	GraphDatabaseService graphDb;

	public XmlTreeServiceGraph(GraphDatabaseService graphb) {
		this.graphDb = graphb;
	}

	@Override
	public List<XmlElement> getTags(String tagName) {
		List<XmlElement> elements = new ArrayList<>();
		GlobalGraphOperations globalGraph = getGlobalGraphOperations();
		Label label = DynamicLabel.label(tagName);
		ResourceIterable<Node> nodes;
		try (Transaction tx = graphDb.beginTx()) {
			nodes = globalGraph.getAllNodesWithLabel(label);
			for (Node node : nodes) {
				XmlElement element = getXmlElement(node);
				elements.add(element);
			}
			tx.success();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}

		return elements;
	}

	@Override
	public List<XmlElement> getChildren(XmlElement parent) {
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		List<XmlElement> childElementList = new ArrayList<>();

		try(Transaction tx = graphDb.beginTx()) {
			Node node = graphDb.getNodeById(parent.getId());
			Iterable<Relationship> childRelations = node.getRelationships(Direction.OUTGOING);
			Iterator<Relationship> relationshipIterator = childRelations.iterator();
			while(relationshipIterator.hasNext()) {
				Relationship relationship = relationshipIterator.next();
				Node childNode = relationship.getStartNode();
				XmlElement childElement = getXmlElement(childNode);
				childElementList.add(childElement);
		      }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return childElementList;
	}

	@Override
	public XmlElement getParent(XmlElement child) {
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		List<XmlElement> parentElementList = new ArrayList<>();

		try(Transaction tx = graphDb.beginTx()) {
			Iterable<Relationship> parentRelations = graphDb.getNodeById(
					child.getId()).getRelationships(Direction.OUTGOING);
			for (Relationship relationship : parentRelations) {
				Node childNode = relationship.getEndNode();
				XmlElement childElement = getXmlElement(childNode);
				parentElementList.add(childElement);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return parentElementList.get(0);
	}

	@Override
	public List<XmlElement> getSiblings(XmlElement element) {
		// TODO Auto-generated method stub
		return null;
	}

	private GlobalGraphOperations getGlobalGraphOperations() {
		return GlobalGraphOperations.at(graphDb);
	}

	private XmlElement getXmlElement(Node node) throws IOException,
			JsonParseException, JsonMappingException {
		XmlElement element = new XmlElement();
		HierarchyIdentifier hierarchyIdentifier = new HierarchyIdentifier();
		ObjectMapper mapper = new ObjectMapper();
		element.setTagName((String) node.getProperty(XmlElements.TAG.getValue()));
		element.setTagValue((String) node.getProperty(XmlElements.VALUE
				.getValue()));
		element.setParentId((int) node.getProperty(XmlElements.PARENT
				.getValue()));
		hierarchyIdentifier.setId((int) node.getProperty(XmlElements.ID
				.getValue()));
		element.setHierarchyIdentifier(hierarchyIdentifier);
		String attributeJSONString = (String) node
				.getProperty(XmlElements.ATTRIBUTES.getValue());
		Map<String, String> attributes = mapper.readValue(attributeJSONString,
				new TypeReference<HashMap<String, String>>() {
				});
		/*
		 * String mapString =
		 * (String)node.getProperty(XmlElements.ATTRIBUTES.getValue());
		 * System.out.println(mapString);
		 * element.setAttributes(splitToMap(mapString));
		 */
		element.setAttributes(attributes);
		return element;
	}
}
