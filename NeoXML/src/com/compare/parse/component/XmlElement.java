package com.compare.parse.component;

import java.util.HashMap;
import java.util.Map;

public class XmlElement {

	private String tagName;
	private String tagValue;
	private Map<String, String> attributes = new HashMap<String,String>();
	private HierarchyIdentifier hierarchyIdentifier;
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagValue() {
		return tagValue;
	}
	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}
	public Map<String, String> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}
	public HierarchyIdentifier getHierarchyIdentifier() {
		return hierarchyIdentifier;
	}
	public void setHierarchyIdentifier(HierarchyIdentifier hierarchyIdentifier) {
		this.hierarchyIdentifier = hierarchyIdentifier;
	}
	
	
}
