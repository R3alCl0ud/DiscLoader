package io.discloader.discloader.core.entity.invite;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;

import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.invite.IInviteGuild;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.GuildJSON;
import io.discloader.discloader.util.DLUtil.Endpoints;

public class InviteGuild implements IInviteGuild {

	private long id;
	public final String name;
	public final String splashHash;
	public final String iconHash;
	public final IIcon icon;
	public final IIcon splash;

	public InviteGuild(GuildJSON data) {
		id = SnowflakeUtil.parse(data.id);
		this.name = data.name;
		this.splashHash = data.splash;
		this.iconHash = data.icon;
		splash = new InviteSplash(data.splash, id);
		icon = new InviteIcon(data.icon, id);
	}

	public class InviteSplash implements IIcon {

		private String hash;
		private long guildID;

		public InviteSplash(String hash, long guildID) {
			this.hash = hash;
			this.guildID = guildID;
		}

		@Override
		public URL toURL() throws MalformedURLException {
			return new URL(Endpoints.guildSplash(guildID, hash));
		}

		@Override
		public String getHash() {
			return hash;
		}
	}

	public class InviteIcon implements IIcon {

		private String hash;
		private long guildID;

		public InviteIcon(String hash, long guildID) {
			this.hash = hash;
			this.guildID = guildID;
		}

		@Override
		public URL toURL() throws MalformedURLException {
			return new URL(Endpoints.guildIcon(guildID, hash));
		}

		@Override
		public String getHash() {
			return hash;
		}
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public OffsetDateTime createdAt() {
		return SnowflakeUtil.creationTime(this);
	}

	@Override
	public IIcon getIcon() {
		return null;
	}

	@Override
	public IIcon getSplash() {
		return splash;
	}

	@Override
	public String getName() {
		return null;
	}

}
