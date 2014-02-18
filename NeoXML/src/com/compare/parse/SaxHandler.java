package com.compare.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compare.parse.component.HierarchyIdentifier;
import com.compare.parse.component.XmlElement;
import com.compare.parse.component.XmlParseStack;

public class SaxHandler extends DefaultHandler{

	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	private int depth;
	private int width;
	
	@Override
	public void endDocument() throws SAXException {
		LOGGER.info("Completed XML parsing--------------------------------");
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		XmlElement element = XmlParseStack.getStack().pop();
		LOGGER.info("Popping from Stack : " + element.getTagName());
		this.depth--;
		this.width++;
		super.endElement(uri, localName, qName);
	}

	@Override
	public void startDocument() throws SAXException {
		LOGGER.info("Starting XML parsing--------------------------------");
		this.depth = 0;
		this.width = 0;
		super.startDocument();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.depth++;
		XmlElement element = createElement(localName,qName,attributes);
		XmlParseStack.getStack().push(element);
		LOGGER.info("Pushing to Stack : " + element.getTagName());
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {		
		super.characters(ch, start, length);
		XmlElement element = XmlParseStack.getStack().pop();
		element.setTagValue(new String(ch, start, length));
		XmlParseStack.getStack().push(element);
	}

	private XmlElement createElement(String localName, String qName,
			Attributes attributes) {
		XmlElement element = new XmlElement();
		Map<String, String> attributeMap = new HashMap<String,String>();
		HierarchyIdentifier hierarchyIdentifier = new HierarchyIdentifier();
		hierarchyIdentifier.setDepth(depth);
		hierarchyIdentifier.setWidth(width);
		element.setTagName(qName);
		for (int i = 0; i < attributes.getLength(); i++) {
			attributeMap.put(attributes.getQName(i), attributes.getValue(i));
		}
		element.setAttributes(attributeMap );
		element.setHierarchyIdentifier(hierarchyIdentifier);
		return element;
	}
	
	

}
