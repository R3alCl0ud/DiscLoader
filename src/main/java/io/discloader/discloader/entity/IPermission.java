package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.IGuildChannel;
import io.discloader.discloader.entity.util.Permissions;

public interface IPermission {

	int toInt();

	IGuildChannel getChannel();

	boolean hasPermission(Permissions permission);

	boolean hasPermission(Permissions permission, boolean explicit);

}
