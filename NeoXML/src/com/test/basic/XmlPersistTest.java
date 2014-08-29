package com.test.basic;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
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
		Path xmlPath = Paths.get("C:/Temp/allianz-s.xml");
		//Path xmlPath = Paths.get("/home/nikhil/dev/xml-sample/discogs_20130801_labels.xml");
		//Path xmlPath = Paths.get("/home/nikhil/dev/xml-sample/books-tiny.xml");
		XmlParseFactory xmlParseFactory= new XmlParseFactory(configPath);
		xmlParseFactory.convertXmlToGraph(xmlPath);
	}
	
	public void testGraphTreeService(){
		Path configFilePath = Paths.get("neo4j.properties");

		XmlParseFactory.initialize(configFilePath);
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		
		XmlTreeServiceGraph treeServiceGraph = new XmlTreeServiceGraph(graphDb);
		System.out.println("-----Test for DOM like methods on XmlGraph----\n");
		List<XmlElement> groupElements = treeServiceGraph.getTags("book");
		System.out.println("Fetching all Book Nodes...");
		for (XmlElement xmlElement : groupElements) {
			System.out.println(xmlElement.getAtrributeString());
		}
		XmlElement firstElement = groupElements.get(0);
		System.out.println("\nFetching children of the first Book Node : " + firstElement.getAtrributeString() + "...");
		List<XmlElement> childrenElements = treeServiceGraph.getChildren(firstElement);
		for (XmlElement xmlElement : childrenElements) {
			System.out.println(xmlElement.getTagName()+ " : " + xmlElement.getTagValue());
		}
		XmlElement child = childrenElements.get(0);
		System.out.println("\nFetching parent of the first child element : "+child.getTagName()+" : "+child.getTagValue() + "...");
		XmlElement parentElement = treeServiceGraph.getParent(child);
		System.out.println(parentElement.getTagName() + " : " + parentElement.getAtrributeString());
		
		System.out.println("\nFetching siblings of the first Book Node : " + firstElement.getAttributes());
		List<XmlElement> elementSibling = treeServiceGraph.getSiblings(firstElement);
		for (XmlElement xmlElement : elementSibling) {
			System.out.println(xmlElement.getTagName() + " : " + xmlElement.getAtrributeString());
		}
	}
	
}
