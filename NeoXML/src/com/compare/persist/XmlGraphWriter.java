package com.compare.persist;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.compare.util.objects.WritableObject;
import com.compare.xml.factory.XmlParseFactory;

public class XmlGraphWriter implements Writer{

	XmlParseFactory xmlParseFactory;
	
	public XmlGraphWriter(){
		Path configPath = Paths.get("neo4j.properties");
		xmlParseFactory= new XmlParseFactory(configPath);
	}
	
	@Override
	public void write(WritableObject writableObject) {
		Path xmlPath = writableObject.getPath();;
		xmlParseFactory.convertXmlToGraph(xmlPath );
	}

}
