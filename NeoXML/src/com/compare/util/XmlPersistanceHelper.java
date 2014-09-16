package com.compare.util;

public class XmlPersistanceHelper {

	public static String removeNamespace(String tagName){
		if(tagName.indexOf(":")>0){
			return tagName.substring(tagName.indexOf(":")+1,tagName.length());
		}
		return tagName;
	}
	
}
