package com.compare.parse.component;

import java.util.Stack;

public class XmlParseStack {

	private static Stack<XmlElement> xmlParseStack = null;

	private XmlParseStack() {
		// Exists only to defeat instantiation.
	}

	public static Stack<XmlElement> getStack() {
		if (xmlParseStack == null) {
			xmlParseStack = new Stack<XmlElement>();
		}
		return xmlParseStack;
	}

}
