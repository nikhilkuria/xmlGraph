package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.compare.parse.component.XmlElement;
import com.compare.util.LocalCacheManager;

public class MinimalGraphWriter extends GraphWriter{

	@Override
	public void writeXmlElements(Map<Integer, XmlElement> elementsMap) {
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		List<XmlElement> elements = new ArrayList<>(elementsMap.values()) ;
		Map<Integer,Long> xmlElementMapping = new HashMap<Integer, Long>();
		Map<Integer,Boolean> xmlElementPersistedMap = new HashMap<Integer,Boolean>();
			try ( Transaction tx = graphDb.beginTx() )
			{		
				for (XmlElement element : elements) {	
					int elementId = element.getHierarchyIdentifier().getId();
					if (!isElementPersisted(xmlElementPersistedMap, elementId)  ) {
						Node node = graphDb.createNode();
						xmlElementMapping.put(elementId, node.getId());
						xmlElementPersistedMap.put(elementId, true);
						setNodeProperties(element, node);
						if (!element.isParent()) {
							Node parentNode;
							XmlElement parentElement = elementsMap.get(element.getParentId());
							int parentId = parentElement.getHierarchyIdentifier().getId();
							if(isElementPersisted(xmlElementPersistedMap, parentId)){								
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
