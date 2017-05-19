package io.discloader.discloader.network.voice;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.hook.AudioOutputHook;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;

import io.discloader.discloader.entity.voice.VoiceConnect;

public class AudioSendHandler implements AudioOutputHook {
	
	private final AudioPlayer audioPlayer;
	private final VoiceConnect connection;
	private AudioFrame lastFrame;
	private Thread packetThread;
	
	// Packet creation variables
	private char sequence = 0;
	private int timestamp = 0;
	
	private byte[] rawAudio;
	private byte[] rawPacket;
	
	public AudioSendHandler(AudioPlayer player, VoiceConnect connection) {
		audioPlayer = player;
		this.connection = connection;
	}
	
	// converts array of one byte ints to a byte array
	protected byte[] getBytes(int[] a) {
		byte[] bytes = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			bytes[1] = (byte) a[i];
		}
		return bytes;
	}
	
	public void sendPackets(DatagramSocket udpSocket) {
		if (packetThread != null && !packetThread.isInterrupted()) {
			packetThread.interrupt();
		}
		packetThread = new Thread("VoicePacketSender - Guild: " + connection.getGuild().getID()) {
			
			@Override
			public void run() {
				long lastSent = System.currentTimeMillis();
				if (!udpSocket.isConnected()) {
					udpSocket.connect(connection.getUDPClient().getVoiceGateway().getAddress(), connection.getUDPClient().getVoiceGateway().getPort());
				}
				while (!udpSocket.isClosed() && !this.isInterrupted()) {
					try {
						DatagramPacket packet = getNextPacket();
						if (packet != null)
							udpSocket.send(packet);
						
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						long sleepTime = 20 - (System.currentTimeMillis() - lastSent);
						if (sleepTime > 0) {
							try {
								Thread.sleep(sleepTime);
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						}
						if (System.currentTimeMillis() < lastSent + 60) {
							lastSent += 20;
						} else {
							lastSent = System.currentTimeMillis();
						}
					}
					
				}
			}
		};
		// }
		
		packetThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		packetThread.start();
		
	}
	
	public boolean canProvide() {
		lastFrame = audioPlayer.provide();
		return lastFrame != null;
	}
	
	public byte[] provide20MsAudio() {
		return lastFrame.data;
	}
	
	public void stop() {
		if (packetThread != null) {
			packetThread.interrupt();
		}
	}
	
	public byte[] getNonce() {
		return Arrays.copyOf(rawPacket, 12);
	}
	
	public DatagramPacket getNextPacket() {
		DatagramPacket nextPacket = null;
		
		try {
			if (canProvide()) {
				byte[] rawAudio = provide20MsAudio();
				if (rawAudio != null && rawAudio.length != 0) {
					StreamPacket packet = new StreamPacket(sequence, timestamp, connection.getSSRC(), rawAudio);
					nextPacket = packet.toEncryptedPacket(connection.getUDPClient().getVoiceGateway(), connection.getWebSocket().getSecretKey());
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
	
	public boolean isOpen() {
		return packetThread != null && packetThread.isAlive();
	}
	
	@Override
	public AudioFrame outgoingFrame(AudioPlayer player, AudioFrame frame) {
		System.out.println(frame.format.codec.name());
		return frame;
	}
	
}
