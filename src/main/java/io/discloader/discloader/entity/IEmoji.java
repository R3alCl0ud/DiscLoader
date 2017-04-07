package io.discloader.discloader.entity;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.util.ISnowflake;

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
