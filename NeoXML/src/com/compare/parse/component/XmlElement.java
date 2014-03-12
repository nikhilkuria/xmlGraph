package com.compare.parse.component;

import java.util.HashMap;
import java.util.Map;
//TODO This should implement an interface
public class XmlElement {

	private String tagName;
	private String tagValue;
	private Map<String, String> attributes = new HashMap<String,String>();
	private HierarchyIdentifier hierarchyIdentifier;
	private int parentId;
	private boolean persisted;
	
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public String getTagValue() {
		if(tagValue==null){
			return "";
		}
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
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getAtrributeString(){
		return this.attributes.toString();
	}
	public boolean isParent(){
		return getParentId() == 0?true :false;
	}
	public boolean isPersisted() {
		return persisted;
	}
	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}
	
	
}
