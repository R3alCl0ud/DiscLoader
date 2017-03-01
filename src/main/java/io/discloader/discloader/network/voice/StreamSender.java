package io.discloader.discloader.network.voice;

import java.net.DatagramSocket;

/**
 * @author Perry Berman
 *
 */
public class StreamSender {
    private final StreamProvider provider;
    private final DatagramSocket udpSocket;
    
    
    public StreamSender(StreamProvider streamer) {
        this.provider = streamer;
        this.udpSocket = streamer.conection.getUdpClient().udpSocket;
    }
    
    
}
