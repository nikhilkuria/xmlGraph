package com.compare.parse;

public class ParseHelper {

	public static int ROOT_ELEMENT_ID = -1;
	
	private static int id = 0;
	
	public static int getId(){
		return id++;
	}
	
}
