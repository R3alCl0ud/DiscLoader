package io.discloader.discloader.entity.guild;

import java.time.OffsetDateTime;

import io.discloader.discloader.entity.IAccount;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ICreationTime;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IIntegration extends ISnowflake, ICreationTime {

	String getType();

	String getName();

	boolean isEnabled();

	boolean isSyncing();

	IRole getSubscriberRole();

	IAccount getAccount();

	IUser getUser();

	OffsetDateTime getSyncedAt();

}
