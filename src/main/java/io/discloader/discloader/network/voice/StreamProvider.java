/**
 * 
 */
package io.discloader.discloader.network.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.iwebpp.crypto.TweetNaclFast.SecretBox;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.hook.AudioOutputHook;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

/**
 * @author Perry Berman
 *
 */
public class StreamProvider implements AudioOutputHook {

    public final VoiceConnection conection;

    private final StreamSender sender;

    private final int HEADER_LENGTH = 24;
    private final int TypeIndex = 0;
    private final int VersionIndex = 1;
    private final int SequenceIndex = 2;
    private final int TimestampIndex = 4;
    private final int SSRCIndex = 8;

    private final byte TYPE = (byte) 0x80;
    private final byte VERSION = (byte) 0x78;

    private byte[] rawAudio;
    private byte[] rawPacket;

    public VoiceWebSocket ws;
    public UDPVoiceClient udpClient;

    public SecretBox nacl;

    public StreamProvider(VoiceConnection connection) {
        this.conection = connection;

        this.sender = new StreamSender(this);

        this.ws = connection.getWs();
        
        this.udpClient = connection.getUdpClient();
    }

    public void start() {
        this.sender.sendPackets();
    }

    public void createNaCl(int[] secretKey) {
        nacl = new SecretBox(getBytes(secretKey));
    }

    // converts array of one byte ints to a byte array
    protected byte[] getBytes(int[] a) {
        byte[] bytes = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            bytes[1] = (byte) a[i];
        }
        return bytes;
    }

    public void generatePacket(char sequence, int SSRC, byte[] rawAudio) {
        this.rawAudio = rawAudio;
        ByteBuffer buffer = ByteBuffer.allocate(rawAudio.length + 12);

        buffer.put(TypeIndex, TYPE);
        buffer.put(VersionIndex, VERSION);

        int timestamp = (int) (System.currentTimeMillis() & 0xFF);
        buffer.putChar(SequenceIndex, sequence);
        buffer.putInt(TimestampIndex, timestamp);
        buffer.putInt(SSRCIndex, SSRC);
        System.arraycopy(rawAudio, 0, buffer.array(), 12, rawAudio.length);

        this.rawPacket = buffer.array();
    }

    public DatagramPacket getEncryptedPacket() {
        
        byte[] nonce = new byte[HEADER_LENGTH];
        
        System.arraycopy(getNonce(), 0, nonce, 0, 12);
        
        byte[] encrypted = nacl.box(getRawAudio(), nonce);
        
        return new DatagramPacket(encrypted, encrypted.length, this.udpClient.voice_gateway);
    }
    
    public byte[] getNonce() {
        return Arrays.copyOf(rawPacket, 12);
    }
    
    
    @Override
    public AudioFrame outgoingFrame(AudioPlayer player, AudioFrame frame) {
        return frame;
    }

    /**
     * @return the rawAudio
     */
    public byte[] getRawAudio() {
        return Arrays.copyOf(this.rawAudio, this.rawAudio.length);
    }

    /**
     * @return the rawPacket
     */
    public byte[] getRawPacket() {
        return Arrays.copyOf(this.rawPacket, this.rawPacket.length);
    }

    public void close() {
        this.sender.close();
    }
    
}
