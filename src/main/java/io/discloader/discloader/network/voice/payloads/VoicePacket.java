/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 *
 */
public class VoicePacket {
    public int op;
    public Object d;
    
    public VoicePacket(int op, Object d) {
        this.op = op;
        this.d = d;
    }
}
