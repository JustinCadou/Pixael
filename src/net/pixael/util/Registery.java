package net.pixael.util;

import java.util.HashMap;
import java.util.List;
import net.fantasticfantasy.mainkit.CollectionUtil;

public class Registery<V> extends HashMap<String, V> {
	
	private static final long serialVersionUID = 1L;
	
	public V put(String key, V value) {
		if (value == null) {
			throw new NullPointerException("'value' is null");
		}
		return super.put(key, value);
	}
	
	public List<V> listValues() {
		return CollectionUtil.collectionToList(super.values());
	}
	
	public List<String> getKeysContaining(String str) {
		for (String s : this.keySet()) {
			
		}
		return null;
	}
}
