package io.discloader.discloader.network.voice;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author Perry Berman
 *
 */
public class StreamPacket {

	private final char seqence;
	private final int timestamp;
	private final int ssrc;

	private final byte[] rawPacket;
	private final byte[] encodedAudio;

	public StreamPacket(DatagramPacket packet) {
		this(Arrays.copyOf(packet.getData(), packet.getLength()));
	}

	public StreamPacket(byte[] data) {
		this.rawPacket = data;
		ByteBuffer buffer = ByteBuffer.wrap(data);
		this.seqence = buffer.getChar(2);
		this.timestamp = buffer.getInt(4);
		this.ssrc = buffer.getInt(8);

		byte[] audio = new byte[buffer.array().length - 12];
		System.arraycopy(buffer.array(), 12, audio, 0, audio.length);
		this.encodedAudio = audio;
	}

}
