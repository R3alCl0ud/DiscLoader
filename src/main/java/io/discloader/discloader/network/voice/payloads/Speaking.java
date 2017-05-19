/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 *
 */
public class Speaking {
    public boolean speaking;
    public int delay;
    
    public Speaking(boolean speaking, int delay) {
        this.speaking = speaking;
        this.delay = delay;
    }
}
