/**
 * 
 */
package io.discloader.discloader.network.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 *
 */
public class AudioStreamer {

    public final VoiceConnection conection;
    
    private final AudioSender sender;
    
    public AudioStreamer(VoiceConnection connection) {
        this.conection = connection;
        
        this.sender = new AudioSender(this);
    }
    
    
    
    
    
}
