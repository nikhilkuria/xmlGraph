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
import com.compare.util.LocalCacheManager;
import com.compare.xml.XmlElements;

public class RegularGraphWriter extends GraphWriter {
	
	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	
	public void writeXmlElements(Map<Integer, XmlElement> elementsMap){
		LocalCacheManager cacheManager = new LocalCacheManager();
		int count = 0;
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		int size = elementsMap.size();
		List<XmlElement> elements = new ArrayList<>(elementsMap.values()) ;
		Map<Integer,Long> xmlElementMapping = new HashMap<Integer, Long>();
		Map<Integer,Boolean> xmlElementPersistedMap = new HashMap<Integer,Boolean>();
		List<List<XmlElement>> slicedList = cacheManager.sliceList(elements);
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


	

	
}
