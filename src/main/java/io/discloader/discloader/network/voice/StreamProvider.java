/**
 * 
 */
package io.discloader.discloader.network.voice;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.iwebpp.crypto.TweetNaclFast.SecretBox;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.hook.AudioOutputHook;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 *
 */
public class StreamProvider implements AudioOutputHook {

	public final VoiceConnection connection;

	private final StreamSender sender;

	private char sequence = 0;
	private int timestamp = 0;

	private byte[] rawAudio;
	private byte[] rawPacket;

	public VoiceWebSocket ws;
	public UDPVoiceClient udpClient;

	public SecretBox nacl;

	public StreamProvider(VoiceConnection connection) {
		this.connection = connection;

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

	public byte[] getNonce() {
		return Arrays.copyOf(rawPacket, 12);
	}

	public DatagramPacket getNextPacket() {
		DatagramPacket nextPacket = null;

		AudioFrame frame = this.connection.player.provide();

		try {
			if (frame != null && ws != null) {
				byte[] rawAudio = frame.data;
				if (rawAudio != null && rawAudio.length != 0) {
					StreamPacket packet = new StreamPacket(sequence, timestamp, connection.getSSRC(), rawAudio);
					nextPacket = packet.toEncryptedPacket(udpClient.voice_gateway, ws.getSecretKey());

					if (sequence + 1 > Character.MAX_VALUE) {
						sequence = 0;
					} else {
						sequence++;
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nextPacket != null) {
			timestamp += 960;
		}

		return nextPacket;
	}

	public boolean isOpen() {
		return this.sender.isOpen();
	}

	@Override
	public AudioFrame outgoingFrame(AudioPlayer player, AudioFrame frame) {
		System.out.println(frame.format.codec.name());
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
