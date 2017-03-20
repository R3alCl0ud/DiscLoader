package io.discloader.discloader.entity.sendable;

/**
 * @author Perry Berman
 *
 */
public class Packet {

	public int op;
	public Object d;

	public Packet(int op, Object d) {
		this.op = op;
		this.d = d;
	}
}
