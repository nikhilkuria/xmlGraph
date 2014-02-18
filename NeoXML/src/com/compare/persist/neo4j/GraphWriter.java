package com.compare.persist.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

import com.compare.parse.component.XmlElement;

public class GraphWriter {

	public void writeXmlElement(XmlElement element){
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		
	}
	
}
