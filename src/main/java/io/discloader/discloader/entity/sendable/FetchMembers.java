package io.discloader.discloader.entity.sendable;

public class FetchMembers {

	public int limit;
	public String after;

	public FetchMembers(int limit, long after) {
		this.limit = limit;
		this.after = Long.toUnsignedString(after, 10);
	}
}
