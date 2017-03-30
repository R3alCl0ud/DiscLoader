package io.discloader.discloader.entity.channel;

import java.util.Map;

import io.discloader.discloader.core.entity.user.User;

public interface IGroupChannel extends ITextChannel, IVoiceChannel {

	Map<String, User> getRecipients();

}
