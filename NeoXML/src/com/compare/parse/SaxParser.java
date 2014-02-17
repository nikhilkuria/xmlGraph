package com.compare.parse;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class SaxParser {

	public void parse(File xmlFile){
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SaxHandler handler = new SaxHandler();
		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(xmlFile, handler);
		} catch (ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
