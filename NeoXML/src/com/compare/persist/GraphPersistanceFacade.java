package com.compare.persist;

import java.util.Map;

import com.compare.parse.component.XmlElement;
import com.compare.persist.neo4j.BatchGraphWriter;
import com.compare.persist.neo4j.GraphWriter;

public class GraphPersistanceFacade {

	public void saveXmlElements(Map<Integer, XmlElement> elementsMap){
		//RegularGraphWriter writer = new RegularGraphWriter();
		GraphWriter writer = new BatchGraphWriter();
		writer.writeXmlElements(elementsMap);
		//writer.writeRelationships(elementsMap);
	}
	
}
