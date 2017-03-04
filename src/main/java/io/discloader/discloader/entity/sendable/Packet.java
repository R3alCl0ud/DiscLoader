package io.discloader.discloader.entity.sendable;

/**
 * @author Perry Berman
 *
 */
public class Packet {

    public int op;
    public int s;
    public Object d;

    public Packet(int op, int s, Object d) {
        this.op = op;
        this.d = d;
        this.s = s;
    }

    public Packet(int op, Object d) {
        this.op = op;
        this.d = d;
    }
}
