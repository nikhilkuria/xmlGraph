package com.compare.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

import com.compare.parse.component.HierarchyIdentifier;
import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class XmlTreeServiceGraph implements XmlTreeService {

	private final static Logger LOGGER = Logger.getLogger(XmlTreeServiceGraph.class.getName());

	GraphDatabaseService graphDb;
	
	public XmlTreeServiceGraph(GraphDatabaseService graphb){
		this.graphDb = graphb;
	}
	
	@Override
	public List<XmlElement> getTags(String tagName) {
		List<XmlElement> elements = new ArrayList<>();
		GlobalGraphOperations globalGraph = getGlobalGraphOperations();
		ObjectMapper mapper = new ObjectMapper();
		Label label = DynamicLabel.label(tagName);
		ResourceIterable<Node> nodes;
		try ( Transaction tx = graphDb.beginTx() )
		{
			nodes = globalGraph.getAllNodesWithLabel(label);
			for (Node node : nodes) {
				XmlElement element = new XmlElement();
				HierarchyIdentifier hierarchyIdentifier = new HierarchyIdentifier();
				
				element.setTagName(tagName);
				element.setTagValue((String) node.getProperty(XmlElements.VALUE.getValue()));
				element.setParentId((int) node.getProperty(XmlElements.PARENT.getValue()));			
				hierarchyIdentifier.setId((int) node.getProperty(XmlElements.ID.getValue()));
				element.setHierarchyIdentifier(hierarchyIdentifier );
				String attributeJSONString = (String) node.getProperty(XmlElements.ATTRIBUTES.getValue());
				Map<String,String>attributes = mapper.readValue(attributeJSONString, 
					    new TypeReference<HashMap<String,String>>(){});
				/*String mapString = (String)node.getProperty(XmlElements.ATTRIBUTES.getValue());
				System.out.println(mapString);
				element.setAttributes(splitToMap(mapString));*/
				element.setAttributes(attributes);
				elements.add(element);
			}
			tx.success();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
		

		return elements;
	}

	private GlobalGraphOperations getGlobalGraphOperations() {
		return GlobalGraphOperations.at(graphDb);
	}

	@Override
	public List<XmlElement> getChildren(XmlElement parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement getParent(XmlElement child) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<XmlElement> getSiblings(XmlElement element) {
		// TODO Auto-generated method stub
		return null;
	}

}
