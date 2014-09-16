package com.compare.util.objects;

import java.nio.file.Path;

public class XmlObject implements WritableObject {
	
	private Path path;
	
	public XmlObject(Path path){
		this.path = path;
	}
	
	@Override
	public Path getPath() {
		return path;
	}

}
