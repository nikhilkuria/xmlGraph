package com.compare.xml;

public enum XmlElements {

	NAME("NAME"),VALUE("VALUE"),ATTRIBUTES("ATTRIBUTES");
	private String value;
	
	private XmlElements(String val){
		this.value = val;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
