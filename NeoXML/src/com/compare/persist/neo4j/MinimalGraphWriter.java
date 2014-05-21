package com.compare.persist.neo4j;

import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.compare.parse.component.XmlElement;

public class MinimalGraphWriter extends GraphWriter{

	@Override
	public void writeXmlElements(Map<Long, XmlElement> elementsMap) {
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
			try ( Transaction tx = graphDb.beginTx() )
			{		
				for (XmlElement element : elementsMap.values()) {	
					long elementId = element.getId();
					if (!isElementPersisted(xmlElementPersistedMap, elementId)  ) {
						Node node = graphDb.createNode();
						updateMetaDataMaps(element, node);
						setNodeProperties(element, node);
						if (!element.isParent()) {
							Node parentNode;
							XmlElement parentElement = elementsMap.get(element.getParentId());
							long parentId = parentElement.getId();
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
