package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import com.compare.parse.component.XmlElement;

public class BatchGraphWriter extends GraphWriter {

	private final static Logger LOGGER = Logger.getLogger(BatchGraphWriter.class.getName());

	List<Integer> persistedElementsList = new ArrayList<>();
	@Override
	public void writeXmlElements(Map<Integer, XmlElement> elementsMap) {
		//TODO currently assuming the datastore exists
		String storeDir = Neo4jDatabaseHandler.getDatabaseLocation();
		
		LOGGER.info("Starting to write "+elementsMap.size()+" elements");
		int i =0;
		BatchInserter inserter = null;
		try {
			inserter = BatchInserters.inserter(storeDir);
			RelationshipType childRelationship = Neo4jHelper.RelationshipTypes.CHILD_OF;
			for (XmlElement element : elementsMap.values()) {
				i++;
				LOGGER.info("Writing element no : "+i);
				long childNode = writeNode(inserter, element);
				persistedElementsList.add(element.getId());
				if(!isParentPersisted(element.getParentId())){
					if (element.getParentId()!=0) {
						XmlElement parentElement = elementsMap.get(element
								.getParentId());
						long parentNode = writeNode(inserter, parentElement);
						inserter.createRelationship(childNode, parentNode,
								childRelationship, null);
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			inserter.shutdown();
		}

	}


	private boolean isParentPersisted(int parentId) {
		return persistedElementsList.contains(parentId)? true : false ;
	}


	private long writeNode(BatchInserter inserter,
			XmlElement element) {
		Map<String, Object> properties = getNodeProperties(element);
		Label nodeLabel = Neo4jHelper.XmlLabels.NODE;
		Label tagLabel = DynamicLabel.label(element.getTagName());
		long childNode = inserter.createNode(properties, nodeLabel,tagLabel);
		return childNode;
	}


}
