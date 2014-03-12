package com.compare.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.tooling.GlobalGraphOperations;

import com.compare.parse.component.HierarchyIdentifier;
import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;

public class XmlTreeServiceGraph implements XmlTreeService {

	GraphDatabaseService graphDb;
	
	public XmlTreeServiceGraph(GraphDatabaseService graphb){
		this.graphDb = graphb;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<XmlElement> getTags(String tagName) {
		List<XmlElement> elements = new ArrayList<>();
		GlobalGraphOperations globalGraph = getGlobalGraphOperations();
		Label label = DynamicLabel.label(tagName);
		ResourceIterable<Node> nodes = globalGraph.getAllNodesWithLabel(label);
		
		for (Node node : nodes) {
			XmlElement element = new XmlElement();
			HierarchyIdentifier hierarchyIdentifier = new HierarchyIdentifier();
			
			element.setTagName(tagName);
			element.setTagValue((String) node.getProperty(XmlElements.VALUE.getValue()));
			element.setParentId((int) node.getProperty(XmlElements.PARENT.getValue()));			
			hierarchyIdentifier.setId((int) node.getProperty(XmlElements.ID.getValue()));
			element.setHierarchyIdentifier(hierarchyIdentifier );
			element.setAttributes((Map<String, String>) node.getProperty(XmlElements.ATTRIBUTES.getValue()));
			elements.add(element);
		}
		return elements;
	}

	private GlobalGraphOperations getGlobalGraphOperations() {
		return GlobalGraphOperations.at(graphDb);
	}

	@Override
	public List<XmlElement> getChildren(XmlElement parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public XmlElement getParent(XmlElement child) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<XmlElement> getSiblings(XmlElement element) {
		// TODO Auto-generated method stub
		return null;
	}

}
