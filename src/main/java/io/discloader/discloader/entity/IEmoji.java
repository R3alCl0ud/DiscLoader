package io.discloader.discloader.entity;

import io.discloader.discloader.common.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public interface IEmoji extends ISnowflake {
	String getName();
	
	DiscLoader getLoader();
	
	@Override
	String toString();
}
