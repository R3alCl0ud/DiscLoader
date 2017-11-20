package io.discloader.discloader.core.entity.invite;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.invite.IInvite;
import io.discloader.discloader.entity.invite.IInviteChannel;
import io.discloader.discloader.entity.invite.IInviteGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.InviteJSON;
import io.discloader.discloader.network.rest.actions.InviteAction;
import io.discloader.discloader.util.DLUtil.Methods;

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
	private IInviteGuild guild;

	/**
	 * The channel this invite is for.
	 */
	private IInviteChannel channel;

	/**
	 * The user who created the invite
	 */
	private IUser inviter;

	/**
	 * The number of times this invite has been used.
	 */
	private int uses;

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

	private String createdAt;

	public Invite(InviteJSON data, DiscLoader loader) {
		code = data.code;
		maxAge = data.max_age;
		maxUses = data.max_uses;
		temporary = data.temporary;
		revoked = data.revoked;
		createdAt = data.created_at;
		channel = new InviteChannel(data.channel);
		guild = new InviteGuild(data.guild);
		// code.ha
	}

	@Override
	public CompletableFuture<IInvite> accept() {
		return new InviteAction(this, Methods.POST).execute();
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Invite))
			return false;
		Invite invite = (Invite) object;

		return (this == invite) || code.equals(invite.code);

	}

	@Override
	public int hashCode() {
		return code == null || code.isEmpty() ? 0 : code.hashCode();
	}

	@Override
	public CompletableFuture<IInvite> delete() {
		return new InviteAction(this, Methods.DELETE).execute();
	}

	@Override
	public IInviteChannel getChannel() {
		return channel;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public OffsetDateTime getCreatedAt() {
		return OffsetDateTime.parse(createdAt);
	}

	@Override
	public IInviteGuild getGuild() {
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
