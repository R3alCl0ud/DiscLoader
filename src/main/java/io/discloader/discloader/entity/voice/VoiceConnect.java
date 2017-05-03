package io.discloader.discloader.entity.voice;

import java.util.logging.Logger;

import io.discloader.discloader.client.logger.DLLogger;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.channel.IGuildVoiceChannel;
import io.discloader.discloader.entity.channel.IVoiceChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.sendable.Packet;
import io.discloader.discloader.entity.sendable.VoiceStateUpdate;
import io.discloader.discloader.network.voice.UDPVoiceClient;
import io.discloader.discloader.network.voice.VoiceWebSocket;

/**
 * @author Perry Berman
 */
public class VoiceConnect {

	private IVoiceChannel channel;
	private VoiceWebSocket ws;
	private UDPVoiceClient udpClient;

	private final Logger logger;

	// private String endpoint;
	private String token;
	private String userID;
	private String sessionID;

	private boolean speaking;

	public VoiceConnect(IVoiceChannel voiceChannel) {
		channel = voiceChannel;
		// ws = new VoiceWebSocket();
		if (getGuild() != null) logger = new DLLogger("VoiceConnection - Guild: " + getGuild().getID()).getLogger();
		else logger = new DLLogger("VoiceConnection - Channel: " + channel.getID()).getLogger();
	}

	public IVoiceChannel getChannel() {
		return channel;
	}

	public IGuild getGuild() {
		if (channel instanceof IGuildVoiceChannel) return ((IGuildVoiceChannel) channel).getGuild();
		return null;
	}

	public DiscLoader getLoader() {
		return channel.getLoader();
	}

	/**
	 * @return the udpClient
	 */
	public UDPVoiceClient getUDPClient() {
		return this.udpClient;
	}

	/**
	 * @return the ws
	 */
	public VoiceWebSocket getWebSocket() {
		return this.ws;
	}

	/**
	 * @return the speaking
	 */
	public boolean isSpeaking() {
		return this.speaking;
	}

	private void sendStateUpdate(IVoiceChannel channel) {
		getLoader().socket.send(new Packet(4, new VoiceStateUpdate(getGuild(), channel, false, false)));
	}

	/**
	 * @param speaking the speaking to set
	 */
	public void setSpeaking(boolean speaking) {
		this.speaking = speaking;
	}

}
