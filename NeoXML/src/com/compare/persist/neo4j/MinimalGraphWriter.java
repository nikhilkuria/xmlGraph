package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
				Set<Long> elementKeys = elementsMap.keySet();
				List<Long> elementKeysList = new ArrayList<>(elementKeys);
				Collections.sort(elementKeysList);
				for (Long id : elementKeysList) {
					XmlElement element = elementsMap.get(id);
					long elementId = element.getId();
					if (!isElementPersisted(xmlElementPersistedMap, elementId)  ) {
						Node childNode = graphDb.createNode();
						updateMetaDataMaps(element, childNode);
						setNodeProperties(element, childNode);
						if (!element.isParent()) {
							Node parentNode;
							XmlElement parentElement = elementsMap.get(element.getParentId());//TODO why don't you just store parentId
							long parentId = parentElement.getId();
							if(isElementPersisted(xmlElementPersistedMap, parentId)){								
								parentNode = graphDb.getNodeById(xmlElementMapping.get(parentId));
							}else{
								parentNode = graphDb.createNode();
								//updateMetaDataMaps(parentElement, parentNode);
							}
							
							setNodeProperties(parentElement, parentNode);//TODO are we doing this again?
							createRelationship(childNode, parentNode, element.getTagName());
						}
					}				    
				}
				tx.success();
				//TODO bad code monkey.. add a catch block here
			}
		
	}

}
