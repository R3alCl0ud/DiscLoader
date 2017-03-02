package io.discloader.discloader.network.voice;

import io.discloader.discloader.entity.voice.VoiceConnection;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class UDPVoiceClient {

	public static final int IDENTIFY = 0;
	public static final int SELECT_PROTOCAL = 1;
	public static final int READY = 2;
	public static final int HEARTBEAT = 3;
	public static final int SESSION_DESCRIPTION = 4;
	public static final int SPEAKING = 5;

	public DatagramSocket udpSocket;

	@SuppressWarnings("unused")
    private final VoiceConnection connection;

	protected InetSocketAddress voice_gateway;

	public UDPVoiceClient(VoiceConnection connection) {
		this.connection = connection;
	}

	public void bindConnection() {
		try {
			this.udpSocket.bind(voice_gateway);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public InetSocketAddress discoverAddress(InetSocketAddress endpoint, int ssrc) {
		try {
			this.udpSocket = new DatagramSocket();
			ByteBuffer buffer = ByteBuffer.allocate(70);
			buffer.putInt(ssrc);
			DatagramPacket discovery = new DatagramPacket(buffer.array(), buffer.array().length, endpoint);
			this.udpSocket.send(discovery);
			DatagramPacket receive = new DatagramPacket(new byte[70], 70);
			udpSocket.setSoTimeout(1000);
			udpSocket.receive(receive);

			byte[] received = receive.getData();

			String ip = new String(receive.getData());
			ip = ip.substring(3, ip.length() - 2);
			ip = ip.trim();

			byte[] ports = new byte[2];
			ports[0] = received[received.length - 1];
			ports[1] = received[received.length - 2];

			int first = (0x000000FF & ((int) ports[0]));
			int second = (0x000000FF & ((int) ports[1]));
			int port = (first << 8) | second;

			this.voice_gateway = endpoint;

			return new InetSocketAddress(ip, port);
		} catch (SocketException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
