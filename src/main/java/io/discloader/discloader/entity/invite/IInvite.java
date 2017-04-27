package io.discloader.discloader.entity.invite;

import java.time.OffsetDateTime;
import java.util.concurrent.CompletableFuture;

import io.discloader.discloader.entity.user.IUser;

/**
 * @author Perry Berman
 */
public interface IInvite {

	CompletableFuture<IInvite> accept();

	CompletableFuture<IInvite> delete();

	String getCode();

	IInviteChannel getChannel();

	IInviteGuild getGuild();

	IUser getInviter();

	int getMaxAge();

	int getMaxUses();

	int getUses();

	boolean isTemporary();

	boolean isValid();

	OffsetDateTime getCreatedAt();

}
