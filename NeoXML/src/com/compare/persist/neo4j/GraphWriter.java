package com.compare.persist.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
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
				LOGGER.info("Processing "+count+" out of "+elements.size());
			    Node node = graphDb.createNode();
			    Label label = DynamicLabel.label(element.getTagName());
			    if(element.getParentId()==0){
			    	node.addLabel(Neo4jHelper.XmlLabels.PARENT);
			    }
			    node.addLabel(Neo4jHelper.XmlLabels.NODE);
			    node.addLabel(label);
			    node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
			    node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
			    node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
			    node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
			    
			}
			tx.success();
		}
		
	}
		
	}
	
	public void writeRelationships(List<XmlElement> elements){
		GraphDatabaseService graphDb = Neo4jDatabaseHandler.getGraphDatabase();
		LOGGER.info("Creating Relationships for "+elements.size()+" nodes");
		try ( Transaction tx = graphDb.beginTx() ){
			for (XmlElement element : elements) {
				if(!element.isParent()){
					Node node = graphDb.findNodesByLabelAndProperty(Neo4jHelper.XmlLabels.NODE, XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId()).iterator().next();
					Node parentNode = graphDb.findNodesByLabelAndProperty(Neo4jHelper.XmlLabels.NODE, XmlElements.ID.getValue(), element.getParentId()).iterator().next();
					node.createRelationshipTo(parentNode, Neo4jHelper.RelationshipTypes.CHILD_OF);
				}

			}
			tx.success();
		}
		LOGGER.info("Completed creating Relationships");
		
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
