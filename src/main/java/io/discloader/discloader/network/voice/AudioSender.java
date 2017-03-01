package io.discloader.discloader.network.voice;

/**
 * @author Perry Berman
 *
 */
public class AudioSender {
    private final AudioStreamer streamer;
    
    public AudioSender(AudioStreamer streamer) {
        this.streamer = streamer;
    }
}
