package io.discloader.discloader.network.voice;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import io.discloader.discloader.entity.voice.VoiceConnection;

/**
 * @author Perry Berman
 */
public class StreamSender {

	private final StreamProvider provider;
	public DatagramSocket udpSocket;
	private Thread packetThread;
	// private VoiceWebSocket ws;
	private final VoiceConnection connection;

	public StreamSender(StreamProvider streamer) {
		this.provider = streamer;
		this.connection = provider.connection;
	}

	public void sendPackets() {
		udpSocket = connection.getUdpClient().udpSocket;
		packetThread = new Thread("Some stream") {

			@Override
			public void run() {
				long lastSent = System.currentTimeMillis();
				while (!udpSocket.isClosed() && !packetThread.isInterrupted()) {
					try {
						if ((System.currentTimeMillis() - lastSent) > 20) {

						}

						DatagramPacket packet = provider.getNextPacket();
						if (packet != null) udpSocket.send(packet);

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

		packetThread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		packetThread.setDaemon(true);
		packetThread.start();

	}

	public void close() {
		if (packetThread != null) {
			packetThread.interrupt();
		}
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return packetThread != null;
	}

}
