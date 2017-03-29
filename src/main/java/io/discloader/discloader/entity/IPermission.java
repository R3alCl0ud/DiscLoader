package io.discloader.discloader.entity;

import io.discloader.discloader.entity.channel.IGuildChannel;

public interface IPermission {

	int toInt();

	IGuildChannel getChannel();
	
	

	boolean hasPermission(Permissions permission);

	boolean hasPermission(Permissions permission, boolean explicit);

}
