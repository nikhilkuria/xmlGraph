package com.compare.parse;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class SaxParser {

	private final static Logger LOGGER = Logger.getLogger(SaxParser.class.getName());
	
	public void parse(File xmlFile){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SaxHandler handler = new SaxHandler();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, handler);
		} catch (ParserConfigurationException | SAXException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
	}
}
