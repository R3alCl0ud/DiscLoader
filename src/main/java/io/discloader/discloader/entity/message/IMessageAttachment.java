package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.util.ISnowflake;

public interface IMessageAttachment extends ISnowflake {

	String getFilename();

	int getHeight();

	String getProxyURL();

	int getSize();

	String getURL();

	int getWidth();

}
