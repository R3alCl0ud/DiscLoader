package io.discloader.discloader.entity.user;

import io.discloader.discloader.entity.ISnowflake;

/**
 * @author Perry Berman
 */
public interface IUserConnection extends ISnowflake {

	String getType();

	String getName();

}
