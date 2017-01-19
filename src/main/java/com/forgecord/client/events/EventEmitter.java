package main.java.com.forgecord.client.events;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;

public class EventEmitter {
	
	public JSONObject listeners = new JSONObject();
	
	public EventEmitter() {
		
	}
	
	@SuppressWarnings("unchecked")
	public EventEmitter on(String event, Object handler) {
		if (!this.listeners.has(event)) {
			this.listeners.put(event, new ArrayList<Object>());
		}
		((ArrayList<Object>) this.listeners.get(event)).add(handler);
		return this;
	}
	
	public void emit(String event, Object... data) {
		if (!this.listeners.has(event)) return;
		ArrayList eventListeners = (ArrayList)this.listeners.get(event);
		for (int i = 0; i < eventListeners.size(); i++) {
			Method handler = (Method) eventListeners.get(i);
			try {
				handler.invoke(data);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
}
