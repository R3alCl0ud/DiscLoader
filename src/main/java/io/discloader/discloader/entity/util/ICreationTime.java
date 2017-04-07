package io.discloader.discloader.entity.util;

import java.time.OffsetDateTime;

import io.discloader.discloader.util.DLUtil;

/**
 * Objects that implement {@link ISnowflake#getID()} should have a method that
 * runs the object's ID through the snowflake reversal function: <code>
 * <br>
 * The reversal function has been implemented in
 * {@link DLUtil#creationTime(ISnowflake)}
 * 
 * @author Perry Berman
 */
public interface ICreationTime {

	/**
	 * @return
	 */
	OffsetDateTime createdAt();

}
