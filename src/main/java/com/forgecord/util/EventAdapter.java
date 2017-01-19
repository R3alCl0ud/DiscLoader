package main.java.com.forgecord.util;

import main.java.com.forgecord.structures.Message;

public abstract class EventAdapter implements EventListener {
	public void Ready() {}
	
	public void Message(Message message) {}
	public void MessageUpdate(Message message) {}
}
