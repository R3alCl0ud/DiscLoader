package io.discloader.discloader.network.voice;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public class UDPVoiceClient {

	public static final int IDENTIFY = 0;
	public static final int SELECT_PROTOCAL = 1;
	public static final int READY = 2;
	public static final int HEARTBEAT = 3;
	public static final int SESSION_DESCRIPTION = 4;
	public static final int SPEAKING = 5;

	public DatagramSocket udpSocket = null;

	private InetSocketAddress voice_gateway;

	public void bindConnection() {
		try {
			System.out.println("trying to bind to address: " + voice_gateway.toString());

			if (udpSocket == null) {
				udpSocket = new DatagramSocket();
				udpSocket.bind(voice_gateway);
			} else {
				if (udpSocket.getRemoteSocketAddress() != null) {
					System.out.println(udpSocket.getRemoteSocketAddress());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public InetSocketAddress discoverAddress(InetSocketAddress endpoint, int ssrc) {
		voice_gateway = endpoint;
//		bindConnection();
		try {
			// if (udpSocket == null) udpSocket = new DatagramSocket();
			udpSocket = new DatagramSocket();
			ByteBuffer buffer = ByteBuffer.allocate(70);
			buffer.putInt(ssrc);
			DatagramPacket discovery = new DatagramPacket(buffer.array(), buffer.array().length, endpoint);
			udpSocket.send(discovery);
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
//			socket.close();
			return new InetSocketAddress(ip, port);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return the voice_gateway
	 */
	public InetSocketAddress getVoice_gateway() {
		return voice_gateway;
	}

	/**
	 * @param voice_gateway the voice_gateway to set
	 */
	public void setVoice_gateway(InetSocketAddress voice_gateway) {
		this.voice_gateway = voice_gateway;
	}

}
