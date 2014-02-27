package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.compare.parse.SaxHandler;
import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;

public class GraphWriter {
	
	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	
	public void writeXmlElements(Map<Integer,XmlElement> elementsMap){
		int count = 0;
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		Collection<XmlElement> elements = elementsMap.values();
		int size = elements.size();
		List<List<XmlElement>> slicedList = sliceList(elements);
		LOGGER.info("Sliced into "+ slicedList.size()+" pieces");
		for (List<XmlElement> slicedElement : slicedList) {
			try ( Transaction tx = graphDb.beginTx() )
			{		
				for (XmlElement element : slicedElement) {	
					XmlElement mapElement = elementsMap.get(element.getHierarchyIdentifier().getId());
					if (!mapElement.isPersisted()) {
						count++;
						LOGGER.info("Processing " + count + " out of " + size);
						Node node = graphDb.createNode();
						mapElement
								.setPersisted(true);
						setNodeProperties(element, node);
						if (!element.isParent()) {
							XmlElement parentElement = elementsMap.get(element
									.getParentId());
							Node parentNode = graphDb.createNode();
							setNodeProperties(parentElement, parentNode);
							node.createRelationshipTo(parentNode,
									Neo4jHelper.RelationshipTypes.CHILD_OF);
						}
					}				    
				}
				tx.success();
			}
		}
		
	}

	private void setNodeProperties(XmlElement element, Node node) {
		Label label = DynamicLabel.label(element.getTagName());
		if(element.isParent()){
			node.addLabel(Neo4jHelper.XmlLabels.PARENT);
		}
		node.addLabel(Neo4jHelper.XmlLabels.NODE);
		node.addLabel(label);
		node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
		node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
		node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
		node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
	}
	
	public void writeRelationships(List<XmlElement> elements){
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		LOGGER.info("Creating Relationships for "+elements.size()+" nodes");
		int count = 0;
		List<List<XmlElement>> slicedList = sliceList(elements);
		for (List<XmlElement> slicedElement : slicedList) {
			try ( Transaction tx = graphDb.beginTx() ){
				for (XmlElement element : slicedElement) {
					count++;
					LOGGER.info("Processing "+count+" out of "+elements.size());
					if(!element.isParent()){
						Node node = graphDb.findNodesByLabelAndProperty(Neo4jHelper.XmlLabels.NODE, XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId()).iterator().next();
						Node parentNode = graphDb.findNodesByLabelAndProperty(Neo4jHelper.XmlLabels.NODE, XmlElements.ID.getValue(), element.getParentId()).iterator().next();
						node.createRelationshipTo(parentNode, Neo4jHelper.RelationshipTypes.CHILD_OF);
					}

				}
				tx.success();
			}
			
		}
		
		LOGGER.info("Completed creating Relationships");
		
	}

	private List<List<XmlElement>> sliceList(Collection<XmlElement> elements) {
		List<List<XmlElement>> slicedList = new ArrayList<List<XmlElement>>();
		List<XmlElement> elementsList = new ArrayList<XmlElement>(elements);
		int totalElements = elements.size();
		
		for (int i = 0; i < totalElements; i=i+50000) {
			int upperBoundry = i+50000;
			if(upperBoundry>totalElements){
				upperBoundry = totalElements;
			}
			slicedList.add(elementsList.subList(i, upperBoundry));
		}
		
		return slicedList;
	}
	
}
