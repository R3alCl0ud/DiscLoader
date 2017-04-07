package io.discloader.discloader.entity.user;

import io.discloader.discloader.entity.util.ISnowflake;

/**
 * @author Perry Berman
 */
public interface IUserConnection extends ISnowflake {

	String getType();

	String getName();

}
