package io.discloader.discloader.entity.channel;

import java.io.File;
import java.util.Map;

import io.discloader.discloader.client.render.util.Resource;
import io.discloader.discloader.entity.IIcon;
import io.discloader.discloader.entity.application.IOAuthApplication;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.rest.RestAction;

public interface IGroupChannel extends ITextChannel {
	
	/**
	 * Removes yourself from this Group DM channel.
	 * 
	 * @return A RestAction object.
	 */
	RestAction<IGroupChannel> leaveGroup();
	
	IIcon getIcon();
	
	String getIconHash();
	
	String getName();
	
	Map<Long, IUser> getRecipients();
	
	IUser getOwner();
	
	long getOwnerID();
	
	long getApplicationID();
	
	RestAction<IOAuthApplication> fetchApplication();
	
	RestAction<IGroupChannel> setName(String name);
	
	RestAction<IGroupChannel> setIcon(String iconFileLocation);
	
	RestAction<IGroupChannel> setIcon(File iconFile);
	
	RestAction<IGroupChannel> setIcon(Resource iconResource);
	
	RestAction<IGroupChannel> removeRecipient(IUser recipient);
	
	RestAction<IGroupChannel> addRecipient(IUser recipient, String accessToken);
	
	RestAction<IGroupChannel> addRecipient(IUser recipient, String accessToken, String nickname);
	
}
