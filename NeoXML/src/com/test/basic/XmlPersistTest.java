package com.test.basic;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import com.compare.xml.factory.XmlParseFactory;

public class XmlPersistTest {

	@Test
	public void testXmlPersist(){
		Path configPath = Paths.get("neo4j.properties");
		Path xmlPath = Paths.get("C:/Temp/allianz.xml");
		XmlParseFactory xmlParseFactory= new XmlParseFactory(configPath );
		xmlParseFactory.convertXmlToGraph(xmlPath);
	}
	
}
