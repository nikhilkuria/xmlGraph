package com.test.basic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;

import com.compare.parse.component.XmlElement;
import com.compare.persist.neo4j.Neo4jDatabaseHandler;
import com.compare.service.XmlTreeServiceGraph;
import com.compare.xml.factory.XmlParseFactory;

public class XmlPersistTest {

//In dev branch
	@Test
	public void testXmlPersist(){
		Path configPath = Paths.get("neo4j.properties");
		//Path xmlPath = Paths.get("C:/Temp/allianz.xml");
		//Path xmlPath = Paths.get("/home/nikhil/dev/xml-sample/discogs_20130801_labels.xml");
		Path xmlPath = Paths.get("/home/nikhil/dev/xml-sample/simple-tiny.xml");
		XmlParseFactory xmlParseFactory= new XmlParseFactory(configPath);
		xmlParseFactory.convertXmlToGraph(xmlPath);
	}
	
	//@Test
	public void testGraphTreeService(){
		Path configFilePath = Paths.get("neo4j.properties");

		XmlParseFactory.initialize(configFilePath);
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		
		XmlTreeServiceGraph treeServiceGraph = new XmlTreeServiceGraph(graphDb);
		
		List<XmlElement> groupElements = treeServiceGraph.getTags("Group");
		
	}
	
}
