package com.compare.parse.component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class XmlElement {

	private final static Logger LOGGER = Logger.getLogger(XmlElement.class.getName());
	
	private String tagName;
	private String tagValue;
	private Map<String, String> attributes = new HashMap<String,String>();
	private HierarchyIdentifier hierarchyIdentifier;
	private long parentId;
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
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getAtrributeString(){
		ObjectMapper jsonMapper = new ObjectMapper();
		try {
			return jsonMapper.writeValueAsString(this.attributes);
		} catch (JsonProcessingException e) {
			LOGGER.severe(e.getMessage());
			e.printStackTrace();
		}
			return null;
	}
	public boolean isParent(){
		return getParentId() == -1?true :false;
	}
	public boolean isPersisted() {
		return persisted;
	}
	public void setPersisted(boolean persisted) {
		this.persisted = persisted;
	}
	public long getId(){
		return this.hierarchyIdentifier.getId();
	}
	public void setId(long id){
		this.hierarchyIdentifier.setId(id);
	}
	
	
}
