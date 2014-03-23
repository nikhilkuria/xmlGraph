package com.compare.util;

import java.util.ArrayList;
import java.util.List;

import com.compare.parse.component.XmlElement;

public class LocalCacheManager {

	public List<List<XmlElement>> sliceList(List<XmlElement> elements) {
		List<List<XmlElement>> slicedList = new ArrayList<List<XmlElement>>();
		int totalElements = elements.size();
		
		for (int i = 0; i < totalElements; i=i+5000) {
			int upperBoundry = i+5000;
			if(upperBoundry>totalElements){
				upperBoundry = totalElements;
			}
			slicedList.add(elements.subList(i, upperBoundry));
		}
		
		return slicedList;
	}
	
}
