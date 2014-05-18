package com.compare.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.compare.parse.component.HierarchyIdentifier;
import com.compare.parse.component.XmlElement;
import com.compare.parse.component.XmlParseStack;
import com.compare.persist.GraphPersistanceFacade;

public class SaxHandler extends DefaultHandler{

	private final static Logger LOGGER = Logger.getLogger(SaxHandler.class.getName());
	private int depth;
	private int width;
	private int count;
	
	private Map<Long,XmlElement> elementsMap = new HashMap<>();
	
	
	@Override
	public void endDocument() throws SAXException {
		GraphPersistanceFacade graphFacade = new GraphPersistanceFacade();
		LOGGER.info("Completed XML parsing--------------------------------");
		graphFacade.saveXmlElements(elementsMap);
		LOGGER.info("Completed Converting elements to map--------------------------------");
		super.endDocument();
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		Stack<XmlElement> stack = XmlParseStack.getStack();
		long parentId;
		XmlElement element = stack.pop();
		if(!stack.isEmpty()){
			XmlElement parentElement = stack.pop();
			parentId = parentElement.getHierarchyIdentifier().getId();
			stack.push(parentElement);
		}else{
			parentId =-1;
		}			
		element.setParentId(parentId);
		this.elementsMap.put(element.getHierarchyIdentifier().getId(),element);
		//this.elementsMap.put(element.getHierarchyIdentifier().getId(), element);
		LOGGER.info("Writing element to map "+ count);
		count++;
		//LOGGER.info("Popping from Stack : " + element.getTagName());
		this.depth--;
		this.width++;
		super.endElement(uri, localName, qName);
	}

/*	private String prepareStringFromElement(XmlElement element) {
		String separator = "~";
		0 - id
		1 - tagName
		2 - tagValue
		3 - attributeString
		4 - isParent
		5 - parentId
		
		return element.getHierarchyIdentifier().getId() + separator + element.getTagName() + separator 
				+ element.getTagValue() + separator + element.getAtrributeString() + separator 
				+ element.isParent() + separator + element.getParentId();
	}*/

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
		//LOGGER.info("Pushing to Stack : " + element.getTagName());
		super.startElement(uri, localName, qName, attributes);
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {		
		super.characters(ch, start, length);
		XmlElement element = XmlParseStack.getStack().pop();
		String tagValue = new String(ch, start, length);
		element.setTagValue(tagValue);
		XmlParseStack.getStack().push(element);
	}

	private XmlElement createElement(String localName, String qName,
			Attributes attributes) {
		XmlElement element = new XmlElement();
		Map<String, String> attributeMap = new HashMap<String,String>();
		HierarchyIdentifier hierarchyIdentifier = new HierarchyIdentifier();
		hierarchyIdentifier.setDepth(depth);
		hierarchyIdentifier.setWidth(width);
		hierarchyIdentifier.setId(ParseHelper.getId());
		element.setTagName(qName);
		for (int i = 0; i < attributes.getLength(); i++) {
			attributeMap.put(attributes.getQName(i), attributes.getValue(i));
		}
		element.setAttributes(attributeMap );
		element.setHierarchyIdentifier(hierarchyIdentifier);
		return element;
	}
	
	

}
