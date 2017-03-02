package io.discloader.discloader.network.voice;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.iwebpp.crypto.TweetNaclFast.SecretBox;

/**
 * @author Perry Berman
 *
 */
public class StreamPacket {

	private final int HEADER_LENGTH = 24;
	private final int NONCE_LENGTH = 12;
	private final int TypeIndex = 0;
	private final int VersionIndex = 1;
	private final int SequenceIndex = 2;
	private final int TimestampIndex = 4;
	private final int SSRCIndex = 8;

	private final byte TYPE = (byte) 0x80;
	private final byte VERSION = (byte) 0x78;

	private final char sequence;
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
		this.sequence = buffer.getChar(2);
		this.timestamp = buffer.getInt(4);
		this.ssrc = buffer.getInt(8);

		byte[] audio = new byte[buffer.array().length - 12];
		System.arraycopy(buffer.array(), 12, audio, 0, audio.length);
		this.encodedAudio = audio;
	}

	public StreamPacket(char sequence, int timestamp, int ssrc, byte[] encodedAudio) {
		this.encodedAudio = encodedAudio;
		this.sequence = sequence;
		this.timestamp = timestamp;
		this.ssrc = ssrc;
		ByteBuffer buffer = ByteBuffer.allocate(encodedAudio.length + NONCE_LENGTH);

		buffer.put(TypeIndex, TYPE);
		buffer.put(VersionIndex, VERSION);
		buffer.putChar(SequenceIndex, sequence);
		buffer.putInt(TimestampIndex, timestamp);
		buffer.putInt(SSRCIndex, ssrc);
		System.arraycopy(encodedAudio, 0, buffer.array(), NONCE_LENGTH, encodedAudio.length);

		this.rawPacket = buffer.array();
	}

	public DatagramPacket toPacket(InetSocketAddress address) {
		return new DatagramPacket(getRawPacket(), rawPacket.length, address);
	}

	public DatagramPacket toEncryptedPacket(InetSocketAddress address, byte[] secretKey) {
		byte[] nonce = new byte[12];
		
		System.arraycopy(getRawPacket(), 0, nonce, 0, 12);
		
		SecretBox nacl = new SecretBox(secretKey);
		byte[] encryptedAudio = nacl.box(getEncodedAudio(), nonce);
		
		return new StreamPacket(sequence, timestamp, ssrc, encryptedAudio).toPacket(address);
	}
	
	public byte[] getEncodedAudio() {
		return Arrays.copyOf(this.encodedAudio, this.encodedAudio.length);
	}

	public byte[] getNonce() {
		return Arrays.copyOf(rawPacket, 12);
	}

	public byte[] getRawPacket() {
		return Arrays.copyOf(rawPacket, rawPacket.length);
	}
	
}
