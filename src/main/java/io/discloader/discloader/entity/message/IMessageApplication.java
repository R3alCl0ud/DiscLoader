package io.discloader.discloader.entity.message;

import io.discloader.discloader.entity.util.ISnowflake;

/**
 * @author Perry Berman
 *
 */
public interface IMessageApplication extends ISnowflake {
	public String getDescription();

	public String getName();

	public String getIcon();

	public String getCoverImage();

}
