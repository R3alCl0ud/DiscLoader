package io.discloader.discloader.entity.channel;

import java.util.Map;

import io.discloader.discloader.entity.user.IUser;

public interface IGroupChannel extends ITextChannel {

	Map<Long, IUser> getRecipients();


}
