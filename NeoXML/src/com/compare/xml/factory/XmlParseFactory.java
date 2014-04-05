package com.compare.xml.factory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Properties;

import org.neo4j.helpers.collection.MapUtil;

import com.compare.parse.SaxParser;
import com.compare.persist.neo4j.Neo4jHelper;

public class XmlParseFactory {

	SaxParser parser;
	public static Map<String,String> neo4jConfig;
	
	public static void initialize(Path configFilePath){
		Properties prop = new Properties();
		try(InputStream input = Files.newInputStream(configFilePath, StandardOpenOption.READ)) { 
			neo4jConfig = MapUtil.load( input );
			prop.load(input);
			Neo4jHelper.neo4jLocation = neo4jConfig.get("db.location");
			// get the property value and print it out	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	
	}
	
	public XmlParseFactory(Path configFilePath){
		XmlParseFactory.initialize(configFilePath);
		parser = new SaxParser();	 
	}
	
	public boolean convertXmlToGraph(Path xmlPath){
		File xmlFile = xmlPath.toFile();
		parser.parse(xmlFile);
		return true;
	}
	
}
