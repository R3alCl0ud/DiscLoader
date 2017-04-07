package io.discloader.discloader.entity.invite;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IInvite;
import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.util.DLUtil.ChannelTypes;

/**
 * Represents an invite object in Discord's API
 * 
 * @author Perry Berman
 * @since 0.1.0
 */
public class Invite implements IInvite {

	/**
	 * The invite code (unique ID)
	 */
	public final String code;

	/**
	 * The guild this invite is for
	 */
	public final IGuild guild;

	/**
	 * The channel this invite is for.
	 */
	public final IGuildChannel channel;

	/**
	 * The user who created the invite
	 */
	public final IUser inviter;

	/**
	 * The number of times this invite has been used.
	 */
	public int uses;

	/**
	 * The max number of times this invite can be used.
	 */
	public int maxUses;
	/**
	 * The duration (in seconds) after which the invite expires.
	 */
	public int maxAge;

	/**
	 * Whether this invite only grants temporary membership.
	 */
	public boolean temporary;

	/**
	 * Whether this invite is revoked.
	 */
	public boolean revoked;

	public Invite(InviteJSON data, DiscLoader loader) {
		this.code = data.code;
		this.guild = loader.guilds.get(data.guild.id);
		if (data.channel.type == ChannelTypes.voice) {
			this.channel = guild.getVoiceChannels().get(data.channel.id);
		} else {
			this.channel = guild.getTextChannels().get(data.channel.id);
		}
		if (!loader.users.containsKey(data.inviter.id)) {
			this.inviter = loader.addUser(data.inviter);
		} else {
			this.inviter = loader.users.get(data.inviter.id);
		}

		this.maxAge = data.max_age;
		this.maxUses = data.max_uses;
		this.temporary = data.temporary;
		this.revoked = data.revoked;
	}

	@Override
	public long getID() {
		return SnowflakeUtil.parse(code);
	}

	@Override
	public IGuildChannel getChannel() {
		return channel;
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public IUser getInviter() {
		return inviter;
	}

	@Override
	public int getMaxAge() {
		return maxAge;
	}

	@Override
	public int getMaxUses() {
		return maxUses;
	}

	@Override
	public int getUses() {
		return uses;
	}

	@Override
	public boolean isTemporary() {
		return temporary;
	}

	@Override
	public boolean isValid() {
		return !revoked;
	}

 
}
