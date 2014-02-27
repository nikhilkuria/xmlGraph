package com.compare.persist.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class Neo4jDatabaseHandler {

	private static GraphDatabaseService graphdb;
	
	private Neo4jDatabaseHandler(){
		
	}
	
	private static String getDatabaseLocation(){
		return Neo4jHelper.neo4jLocation;
	}
	
	public static GraphDatabaseService getGraphDatabase(){
		if(graphdb==null){
			Neo4jDatabaseHandler.graphdb = new GraphDatabaseFactory().newEmbeddedDatabase(getDatabaseLocation());
			registerShutdownHook( graphdb );
			return Neo4jDatabaseHandler.graphdb;
		}else{
			return graphdb;
		}
	}
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
}
