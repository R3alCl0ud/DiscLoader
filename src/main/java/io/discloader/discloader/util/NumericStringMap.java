package io.discloader.discloader.util;

import java.util.Collection;
import java.util.HashMap;

/**
 * Custom hax thingy that
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class NumericStringMap<T> {

	private HashMap<Integer, T> data;
	private HashMap<String, Integer> idBySpecial;
	private HashMap<Integer, String> specialIDs;
	private HashMap<String, Integer> idByChar;
	private HashMap<Integer, String> charIDs;
	private HashMap<Integer, Integer> ids;

	public NumericStringMap() {
		this.data = new HashMap<Integer, T>();
		this.charIDs = new HashMap<Integer, String>();
		this.idByChar = new HashMap<String, Integer>();
		this.specialIDs = new HashMap<Integer, String>();
		this.idBySpecial = new HashMap<String, Integer>();
		this.ids = new HashMap<Integer, Integer>();
	}

	public void set(int id, String charID, String specialID, T data) {
		this.data.put(id, data);
		this.ids.put(id, id);
		this.charIDs.put(id, charID);
		this.specialIDs.put(id, specialID);
		this.idBySpecial.put(specialID, id);
	}

	public void addObject(int id, String charID, T item) {
		 this.set(id,charID, charID, item);
	}

	public String getCharID(int id) {
		return this.charIDs.get(id);
	}

	public String getSpecialID(int id) {
		return this.specialIDs.get(id);
	}

	public T get(int id) {
		return this.data.get(id);
	}

	public T get(String id) {
		int idNum = this.idByChar.getOrDefault(id, -1);
		if (idNum == -1) {
			idNum = this.idBySpecial.getOrDefault(id, -1);
		}
		return this.get(idNum);
	}

	public boolean containsID(int id) {
		return this.ids.containsKey(id);
	}

	public boolean containsCharID(String charID) {
		return this.idByChar.containsKey(charID);
	}

	public boolean containsSpecialID(String specialID) {
		return this.idBySpecial.containsKey(specialID);
	}
	
	public Collection<T> entries() {
		return this.data.values();
	}

}
