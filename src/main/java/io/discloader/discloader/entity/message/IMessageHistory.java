package io.discloader.discloader.entity.message;

import java.util.Collection;

/**
 * @author Perry Berman
 */
public interface IMessageHistory<T extends IMessage> extends Collection<T> {

	T getMessageByID(long messageID);

	T getMessageByID(String messageID);

}
