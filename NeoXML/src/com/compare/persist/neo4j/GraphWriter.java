package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.Collection;
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
import com.compare.xml.XmlElements;

public class GraphWriter {
	
	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	
	public void writeXmlElements(Map<Integer,String> elementsMap){
		int count = 0;
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		Collection<String> elements = elementsMap.values();
		int size = elements.size();
		//TODO purpose is lost if this map grows to a monster
		Map<Integer,Long> xmlElementMapping = new HashMap<Integer, Long>();
		Map<Integer,Boolean> xmlElementPersistedMap = new HashMap<Integer,Boolean>();
		List<List<String>> slicedList = sliceList(elements);
		LOGGER.info("Sliced into "+ slicedList.size()+" pieces");
		for (List<String> slicedElement : slicedList) {
			try ( Transaction tx = graphDb.beginTx() )
			{		
				for (String element : slicedElement) {	
					//int id = element.getHierarchyIdentifier().getId();
					String [] elementSplit = element.split("~");
					int elementID = Integer.valueOf(elementSplit[0]);
					String mapElement = elementsMap.get(elementID);
					if (!isElementPersisted(xmlElementPersistedMap, elementID)  ) {
						count++;
						LOGGER.info("Processing " + count + " out of " + size);
						Node node = graphDb.createNode();
						xmlElementMapping.put(elementID, node.getId());
						/*mapElement
								.setPersisted(true);*/
						xmlElementPersistedMap.put(elementID, true);
						setNodeProperties(elementSplit, node);
						if (!isElementParent(elementSplit)) {
							Node parentNode;
							String parentElement = elementsMap.get(Integer.valueOf(elementSplit[5]));
							String [] parentElementSplit = parentElement.split("~");
							int parentId = Integer.valueOf(parentElementSplit[0]);
							if(isElementPersisted(xmlElementPersistedMap, parentId)){
								LOGGER.info("Already persisted...");
								
								parentNode = graphDb.getNodeById(xmlElementMapping.get(parentId));
							}else{
								parentNode = graphDb.createNode();
							}
							
							setNodeProperties(parentElementSplit, parentNode);
							node.createRelationshipTo(parentNode,
									Neo4jHelper.RelationshipTypes.CHILD_OF);
						}
					}				    
				}
				tx.success();
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

	private void setNodeProperties(String[] elementSplit, Node node) {
		Label label = DynamicLabel.label(elementSplit[1]);
		boolean isParent = isElementParent(elementSplit);
		if(isParent){
			node.addLabel(Neo4jHelper.XmlLabels.PARENT);
		}
		node.addLabel(Neo4jHelper.XmlLabels.NODE);
		node.addLabel(label);
		node.setProperty(XmlElements.VALUE.getValue(), elementSplit[2]);
		node.setProperty(XmlElements.PARENT.getValue(), elementSplit[5]);
		node.setProperty(XmlElements.ID.getValue(), elementSplit[0]);
		node.setProperty(XmlElements.ATTRIBUTES.getValue(), elementSplit[3]);
/*		node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
		node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
		node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
		node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());*/
	}

	private Boolean isElementParent(String[] elementSplit) {
		return elementSplit[4].equals("true")? Boolean.TRUE : Boolean.FALSE;
	}
	
	private List<List<String>> sliceList(Collection<String> elements) {
		List<List<String>> slicedList = new ArrayList<List<String>>();
		List<String> elementsList = new ArrayList<String>(elements);
		int totalElements = elements.size();
		
		for (int i = 0; i < totalElements; i=i+5000) {
			int upperBoundry = i+5000;
			if(upperBoundry>totalElements){
				upperBoundry = totalElements;
			}
			slicedList.add(elementsList.subList(i, upperBoundry));
		}
		
		return slicedList;
	}
	
}
