/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 *
 */
public class VoiceUDPBegin {
    public VoiceData data;
    public String protocol = "udp";
    
    public VoiceUDPBegin(VoiceData data) {
        this.data = data;
    }
}
