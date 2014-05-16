package com.compare.persist.neo4j;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

import com.compare.parse.component.XmlElement;
import com.compare.xml.XmlElements;

public abstract class GraphWriter {

	public abstract void writeXmlElements(Map<Integer, XmlElement> elementsMap);
	
	public boolean isElementPersisted(
			Map<Integer, Boolean> xmlElementPersistedMap, int id) {
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
		node.setProperty(XmlElements.ATTRIBUTES.getValue(), element.getAtrributeString());
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
}
