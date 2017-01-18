package util;

import structures.Message;

public abstract class EventAdapter implements EventListener {
	public void Ready() {}
	
	public void Message(Message message) {}
	public void MessageUpdate(Message message) {}
}
