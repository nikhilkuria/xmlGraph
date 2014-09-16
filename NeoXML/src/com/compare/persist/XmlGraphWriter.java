package com.compare.persist;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.compare.util.objects.WritableObject;
import com.compare.xml.factory.XmlParseFactory;

public class XmlGraphWriter implements GraphDbWriter{

	XmlParseFactory xmlParseFactory;
	
	public XmlGraphWriter(){
		Path configPath = Paths.get("neo4j.properties");
		xmlParseFactory= new XmlParseFactory(configPath);
	}
	
	@Override
	public void write(WritableObject writableObject,PersistanceConfig config) {
		Path xmlPath = writableObject.getPath();;
		xmlParseFactory.convertXmlToGraph(xmlPath,config);
	}

}
