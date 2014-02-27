package com.compare.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

import com.compare.log.LogWriter;
import com.compare.parse.SaxParser;
import com.compare.persist.neo4j.Neo4jHelper;

public class CompareXml {

	private final static Logger LOGGER = Logger.getLogger(CompareXml.class.getName());
	
	public static void main(String args[]){
		//TODO clean up main
		
		try {
			LogWriter.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initProperties();
		SaxParser parser = new SaxParser();
		
		File xmlFile = new File("C:\\Temp\\allianz.xml");
		parser.parse(xmlFile );
		
	}

	private static void initProperties() {
//TODO clean up method body
		Properties prop = new Properties();
		InputStream input = null;
		try {
			 
			input = new FileInputStream("neo4j.properties");
	 
			// load a properties file
			prop.load(input);
			Neo4jHelper.neo4jLocation = prop.getProperty("db.location");
			// get the property value and print it out
;
	 
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
	}
	
	
}
