package io.discloader.discloader.util;

import java.util.Collection;
import java.util.HashMap;

/**
 * Custom hax thing that stores stuff
 * 
 * @author Perry Berman
 * @since 0.0.1
 */
public class DLNameSpacedMap<T> {

	private HashMap<Integer, T> data;
	private HashMap<T, Integer> ids;
	private HashMap<String, Integer> idBySpecial;
	private HashMap<String, Integer> idByChar;
	private HashMap<Integer, String> specialIDs;
	private HashMap<Integer, String> charIDs;

	public DLNameSpacedMap() {
		this.data = new HashMap<Integer, T>();
		this.ids = new HashMap<T, Integer>();
		this.idBySpecial = new HashMap<String, Integer>();
		this.idByChar = new HashMap<String, Integer>();
		this.specialIDs = new HashMap<Integer, String>();
		this.charIDs = new HashMap<Integer, String>();
	}

	public void set(int id, String charID, String specialID, T data) {
		this.data.put(id, data);
		this.ids.put(data, id);
		this.charIDs.put(id, charID);
		this.idByChar.put(charID, id);
		this.specialIDs.put(id, specialID);
		this.idBySpecial.put(specialID, id);
	}

	public void addObject(int id, String charID, T item) {
		this.set(id, charID, charID, item);
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

	public boolean containsID(String id) {
		if (this.idByChar.containsKey(id))
			return true;
		if (this.idBySpecial.containsKey(id))
			return true;
		return false;
	}

	public Collection<String> collectionCharIDs() {
		return this.charIDs.values();
	}

	public Collection<String> collectionSpecialIDs() {
		return this.charIDs.values();
	}

	public Collection<T> entries() {
		return this.data.values();
	}

}
