package com.compare.persist.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;

public abstract class GraphWriter {

	Map<Long,Long> xmlElementMapping = new HashMap<Long, Long>();
	Map<Long,Boolean> xmlElementPersistedMap = new HashMap<Long,Boolean>();
	
	public abstract void writeXmlElements(Map<Long, XmlElement> elementsMap);
	
	public boolean isElementPersisted(
			Map<Long, Boolean> xmlElementPersistedMap, long id) {
		if(xmlElementPersistedMap.get(id)==null){
			return false;
		}
		if(xmlElementPersistedMap.get(id).equals(Boolean.FALSE)){
			return false;
		}
		return true;
	}

	public void setNodeProperties(XmlElement element, Node node) {
		Label label = DynamicLabel.label(element.getTagName());
		boolean isParent = element.isParent();
		if(isParent){
			node.addLabel(Neo4jHelper.XmlLabels.PARENT);
		}
		node.addLabel(Neo4jHelper.XmlLabels.NODE);
		node.addLabel(label);
		node.setProperty(XmlElements.VALUE.getValue(), element.getTagValue());
		node.setProperty(XmlElements.PARENT.getValue(), element.getParentId());
		node.setProperty(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
		//node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
		for (String attributeName : element.getAttributes().keySet()) {
			node.setProperty(attributeName, element.getAttributes().get(attributeName));
		}
		node.setProperty(XmlElements.TAG.getValue(), element.getTagName());
	}
	

	public Map<String, Object> getNodeProperties(XmlElement element) {
		Map<String, Object> properties = new HashMap<>();
		properties.put(XmlElements.VALUE.getValue(), element.getTagValue());
		properties.put(XmlElements.PARENT.getValue(), element.getParentId());
		properties.put(XmlElements.ID.getValue(), element.getHierarchyIdentifier().getId());
		properties.put(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
		return properties;
	}
	
	public void updateMetaDataMaps(XmlElement element, Node node) {
		long elementId = element.getId();
		long nodeId = node.getId();
		xmlElementMapping.put(elementId, nodeId);
		//element.getHierarchyIdentifier().setId(nodeId);
		xmlElementPersistedMap.put(elementId, true);
	}
	

	public void createRelationship(Node childNode, Node parentNode, String relationshipName) {
		RelationshipType relationship = DynamicRelationshipType.withName(relationshipName);
		parentNode.createRelationshipTo(childNode, relationship );
	}
}
