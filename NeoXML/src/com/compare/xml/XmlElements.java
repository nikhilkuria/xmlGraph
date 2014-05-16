package com.compare.xml;

public enum XmlElements {

	NAME("NAME"),VALUE("VALUE"),ATTRIBUTES("ATTRIBUTES"),PARENT("PARENT"),ID("ID"),TAG("TAG");
	private String value;
	
	private XmlElements(String val){
		this.value = val;
	}
	
	public String getValue(){
		return this.value;
	}
	
}
