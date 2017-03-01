/**
 * 
 */
package io.discloader.discloader.network.voice;

import java.nio.ByteBuffer;

import com.iwebpp.crypto.TweetNaclFast.Box;
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

	public final VoiceConnection conection;

	private final StreamSender sender;

	private final int HEADER_LENGTH = 24;
	private final int TypeIndex = 0;
	private final int VersionIndex = 1;
	private final int SequenceIndex = 2;
	private final int TimestampIndex = 4;
	private final int SSRCIndex = 8;

	private final int TYPE = 0x80;
	private final int VERSION = 0x78;

	private VoiceWebSocket ws;

	private SecretBox nacl;

	public StreamProvider(VoiceConnection connection) {
		this.conection = connection;

		this.sender = new StreamSender(this);

		this.ws = connection.getWs();
	}

	public void createNaCl(int[] secretKey) {
		nacl = new SecretBox(getBytes(secretKey));
	}

	// converts array of one byte ints to a byte array
	public byte[] getBytes(int[] a) {
		byte[] bytes = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			bytes[1] = (byte) a[i];
		}
		return bytes;
	}

	public byte[] provideEncryptedHeader(int SSRC) {
		byte[] data = new byte[24];

		data[TypeIndex] = (byte) (TYPE & 0xFF);
		data[VersionIndex] = (byte) (VERSION & 0xFF);
		data[SequenceIndex] = (byte) ((this.ws.getSequence() >> 8) & 0xFF);
		data[3] = (byte) ((this.ws.getSequence()) & 0xFF);

		int timestamp = (int) (System.currentTimeMillis() & 0xFF);

		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.putInt(timestamp);
		byte[] time = buf.array();
		data[TimestampIndex] = time[3];
		data[5] = time[2];
		data[6] = time[1];
		data[7] = time[0];
		buf = ByteBuffer.allocate(4);
		buf.putInt(SSRC);
		byte[] ssrc = buf.array();
		data[SSRCIndex] = ssrc[3];
		data[9] = ssrc[2];
		data[10] = ssrc[1];
		data[11] = ssrc[0];
		return nacl.box(data);
	}

	@Override
	public AudioFrame outgoingFrame(AudioPlayer player, AudioFrame frame) {

		return frame;
	}

}
