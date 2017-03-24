package io.discloader.discloader.network.gateway.packets;

import com.google.gson.Gson;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.network.gateway.DiscSocket;
import io.discloader.discloader.util.DLUtil.Status;

/**
 * @author Perry Berman
 *
 */
public abstract class AbstractHandler {
	public DiscSocket socket;
	public DiscLoader loader;
	public Gson gson;

	public AbstractHandler(DiscSocket socket) {
		this.socket = socket;
		this.loader = this.socket.loader;
		this.gson = new Gson();
	}

	public void handle(SocketPacket packet) {

	}

	public boolean shouldEmit() {
		return loader.ready && socket.status == Status.READY;
	}

}
