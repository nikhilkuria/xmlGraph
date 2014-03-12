/*package com.test.performace;

import java.io.File;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Test;

import com.compare.parse.SaxParser;
import com.compare.xml.CompareXml;

public class CompareXmlTest {

	public ContiPerfRule rule = new ContiPerfRule();
	
	@Test
	@PerfTest(invocations = 100, threads = 20)
	@Required(max = 12, average = 2)
	public void saxParseTest(){
		SaxParser parser = new SaxParser();		
		CompareXml.initProperties();
		File xmlFile = new File("/home/nikhil/dev/xml-sample/simple-small.xml");
		parser.parse(xmlFile);
	}
	
	
}
*/