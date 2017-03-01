/**
 * 
 */
package io.discloader.discloader.network.voice.payloads;

/**
 * @author Perry Berman
 *
 */
public class VoiceData {
    public String address;
    public int port;
    public String mode = "xsalsa20_poly1305";
    
    public VoiceData(String address, int port) {
        this.address = address;
        this.port = port;
    }
}
