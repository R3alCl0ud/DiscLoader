package io.discloader.discloader.entity.sendable;

public class FetchMembers {
	public int limit;
	public String after;
	
	public FetchMembers(int limit, String after) {
		this.limit = limit;
		this.after = after;
	}
}
