package io.disc.DiscLoader.socket.packets;

public class SocketPacket {
	public int op;
	public int s;
	public String t;
	public String[] _trace;
	public int heartbeat_interval;
	public D d;
	public SocketPacket() {

	}
	public class D {
		public int v;
		public String name;
		public User user;
		public class User {
			String id;
			String email;
			String password;
			String username;
			String avatar;
			String discriminator;
			boolean bot;
			boolean verified;
			boolean mfa_enabled;
		}
	}
}



