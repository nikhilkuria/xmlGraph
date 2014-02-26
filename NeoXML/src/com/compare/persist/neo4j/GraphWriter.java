package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.compare.parse.SaxHandler;
import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;

public class GraphWriter {
	
	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	
	public void writeXmlElements(List<XmlElement> elements){
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		int count = 0;

		List<List<XmlElement>> slicedList = sliceList(elements);
		LOGGER.info("Sliced into "+ slicedList.size()+" pieces");
		for (List<XmlElement> slicedElement : slicedList) {
			LOGGER.info("Writing a piece");
		try ( Transaction tx = graphDb.beginTx() )
		{		

			for (XmlElement element : slicedElement) {	
				count++;
				//LOGGER.info("Processing "+count+" out of "+elements.size());
			    Node node = graphDb.createNode();
			    node.setProperty(XmlElements.NAME.getValue(), element.getTagName());
			    node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
			    node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
			    node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
			    node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
			    tx.success();
			}
		}
		
	}
		
	}

	private List<List<XmlElement>> sliceList(List<XmlElement> elements) {
		List<List<XmlElement>> slicedList = new ArrayList<List<XmlElement>>();
		int totalElements = elements.size();
		
		for (int i = 0; i < totalElements; i=i+50000) {
			int upperBoundry = i+50000;
			if(upperBoundry>totalElements){
				upperBoundry = totalElements;
			}
			slicedList.add(elements.subList(i, upperBoundry));
		}
		
		return slicedList;
	}
	
}
