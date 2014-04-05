package com.compare.persist;

import java.util.Map;

import com.compare.parse.component.XmlElement;
import com.compare.persist.neo4j.GraphWriter;
import com.compare.persist.neo4j.MinimalGraphWriter;

public class GraphPersistanceFacade {

	public void saveXmlElements(Map<Integer, XmlElement> elementsMap){
		//RegularGraphWriter writer = new RegularGraphWriter();
		GraphWriter writer = new MinimalGraphWriter();
		writer.writeXmlElements(elementsMap);
		//writer.writeRelationships(elementsMap);
	}
	
}
