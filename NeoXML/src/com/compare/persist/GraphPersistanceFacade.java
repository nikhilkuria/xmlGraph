package com.compare.persist;

import java.util.List;
import java.util.Map;

import com.compare.parse.component.XmlElement;
import com.compare.persist.neo4j.GraphWriter;

public class GraphPersistanceFacade {

	public void saveXmlElements(List<XmlElement> elements){
		GraphWriter writer = new GraphWriter();
		writer.writeXmlElements(elements);
		//writer.writeRelationships(elementsMap);
	}
	
}
