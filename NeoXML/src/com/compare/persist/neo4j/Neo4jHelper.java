package com.compare.persist.neo4j;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

public class Neo4jHelper {
//TODO super crappy pattern
	public static String neo4jLocation;
	
	public enum XmlLabels implements Label { NODE,PARENT };
	enum RelationshipTypes implements RelationshipType
	 {
	    CHILD_OF
	 }
}
