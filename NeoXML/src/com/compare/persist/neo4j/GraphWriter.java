package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
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
	
	public void writeXmlElements(Map<Integer, XmlElement> elementsMap){
		int count = 0;
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		int size = elementsMap.size();
		List<XmlElement> elements = new ArrayList<>(elementsMap.values()) ;
		Map<Integer,Long> xmlElementMapping = new HashMap<Integer, Long>();
		Map<Integer,Boolean> xmlElementPersistedMap = new HashMap<Integer,Boolean>();
		List<List<XmlElement>> slicedList = sliceList(elements);
		LOGGER.info("Sliced into "+ slicedList.size()+" pieces");
		for (List<XmlElement> slicedElement : slicedList) {
			try ( Transaction tx = graphDb.beginTx() )
			{		
				for (XmlElement element : slicedElement) {	
					int elementId = element.getHierarchyIdentifier().getId();
					if (!isElementPersisted(xmlElementPersistedMap, elementId)  ) {
						count++;
						LOGGER.info("Processing " + count + " out of " + size);
						Node node = graphDb.createNode();
						xmlElementMapping.put(elementId, node.getId());
						xmlElementPersistedMap.put(elementId, true);
						setNodeProperties(element, node);
						if (!element.isParent()) {
							Node parentNode;
							XmlElement parentElement = elementsMap.get(element.getParentId());
							int parentId = parentElement.getHierarchyIdentifier().getId();
							if(isElementPersisted(xmlElementPersistedMap, parentId)){
								LOGGER.info("Already persisted...");
								
								parentNode = graphDb.getNodeById(xmlElementMapping.get(parentId));
							}else{
								parentNode = graphDb.createNode();
							}
							
							setNodeProperties(parentElement, parentNode);
							node.createRelationshipTo(parentNode,
									Neo4jHelper.RelationshipTypes.CHILD_OF);
						}
					}				    
				}
				tx.success();
				//TODO bad code monkey.. add a catch block here
			}
		}
		
	}

	private boolean isElementPersisted(
			Map<Integer, Boolean> xmlElementPersistedMap, int id) {
		if(xmlElementPersistedMap.get(id)==null){
			return false;
		}
		if(xmlElementPersistedMap.get(id).equals(Boolean.FALSE)){
			return false;
		}
		return true;
	}

	private void setNodeProperties(XmlElement element, Node node) {
		Label label = DynamicLabel.label(element.getTagName());
		boolean isParent = element.isParent();
		if(isParent){
			node.addLabel(Neo4jHelper.XmlLabels.PARENT);
		}
		node.addLabel(Neo4jHelper.XmlLabels.NODE);
		node.addLabel(label);
		node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
		node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
		node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
		node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
	}
	
	private List<List<XmlElement>> sliceList(List<XmlElement> elements) {
		List<List<XmlElement>> slicedList = new ArrayList<List<XmlElement>>();
		int totalElements = elements.size();
		
		for (int i = 0; i < totalElements; i=i+5000) {
			int upperBoundry = i+5000;
			if(upperBoundry>totalElements){
				upperBoundry = totalElements;
			}
			slicedList.add(elements.subList(i, upperBoundry));
		}
		
		return slicedList;
	}
	
}
