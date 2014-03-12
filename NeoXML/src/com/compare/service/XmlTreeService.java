package com.compare.service;


import java.util.List;

import com.compare.parse.component.XmlElement;

public interface XmlTreeService {

	public List<XmlElement> getTags(String tagName);
	public List<XmlElement> getChildren(XmlElement parent);
	public XmlElement getParent(XmlElement child);
	public List<XmlElement> getSiblings(XmlElement element);
}
