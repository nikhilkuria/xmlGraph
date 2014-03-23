package com.compare.persist.neo4j;

import java.util.Map;
import java.util.logging.Logger;

import org.neo4j.graphdb.Label;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import com.compare.parse.SaxHandler;
import com.compare.parse.component.XmlElement;

public class BatchGraphWriter extends GraphWriter {

	private final static Logger LOGGER = Logger.getLogger(BatchGraphWriter.class.getName());

	
	@Override
	public void writeXmlElements(Map<Integer, XmlElement> elementsMap) {
		//LocalCacheManager cacheManager = new LocalCacheManager();
		//TODO currently assuming the datastore exists
		String storeDir = Neo4jDatabaseHandler.getDatabaseLocation();
		LOGGER.info("Starting to write "+elementsMap.size()+" elements");
		int i =0;
		BatchInserter inserter = BatchInserters.inserter(storeDir);
		Label nodeLabel = Neo4jHelper.XmlLabels.NODE;
		for (XmlElement element : elementsMap.values()) {
			i++;
			LOGGER.info("Writing element no : "+i);
			Map<String, Object> properties = getNodeProperties(element);
			inserter.createNode(properties, nodeLabel);
		}
	}


}
