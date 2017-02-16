package io.discloader.discloader.util;

import java.util.HashMap;

/**
 * Custom hax thingy that 
 * @author Perry Berman
 * @since 0.0.1
 */
public class NumericStringMap<T> {

	private HashMap<Integer, T> data;
	private HashMap<String, Integer> specialIDs;
	private HashMap<String, Integer> charIDs;
	private HashMap<Integer, Integer> ids;
	
	public NumericStringMap() {
		this.data = new HashMap<Integer, T>();
		this.charIDs = new HashMap<String, Integer>();
		this.specialIDs = new HashMap<String, Integer>();
		this.ids = new HashMap<Integer, Integer>();
	}
	
	public void addItemData(int id, String charID, T item) {
		this.ids.put(id, id);
		this.charIDs.put(charID, id);
		this.data.put(id, item);
	}
	
	public String getCharID(int id) {
		for (String key : this.charIDs.keySet()) {
			if (this.charIDs.get(key) == id) {
				return key;
			}
		}
		return null;
	}
	
	public String getSpecialID(int id) {
		for (String key : this.specialIDs.keySet()) {
			if (this.specialIDs.get(key) == id) {
				return key;
			}
		}
		return null;
	}
	
	public T get(int id) {
		return this.data.get(id);
	}
	
	public T get(String id) {
		if (this.charIDs.containsKey(id)) {
			return this.get(this.charIDs.get(id));
		} else if (this.specialIDs.containsKey(id)) {
			return this.get(this.specialIDs.get(id));
		} else {
			return null;
		}
	}
	
	public boolean containsID(int id) {
		return this.ids.containsKey(id);
	}
	
	public boolean containsCharID(String charID) {
		return this.charIDs.containsKey(charID);
	}
	
	public boolean containsSpecialID(String specialID) {
		return this.specialIDs.containsKey(specialID);
	}
	
}
