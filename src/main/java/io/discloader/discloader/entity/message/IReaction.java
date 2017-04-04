package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.IEmoji;

public interface IReaction {

	boolean didReact();

	int getCount();

	IEmoji getEmoji();

	IMessage getMessage();

}
