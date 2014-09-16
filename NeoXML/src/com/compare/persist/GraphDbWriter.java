package com.compare.persist;

import com.compare.util.objects.WritableObject;

public interface GraphDbWriter {

	void write(WritableObject object,PersistanceConfig config);
	
}
