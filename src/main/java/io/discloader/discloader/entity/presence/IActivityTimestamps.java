/**
 * 
 */
package io.discloader.discloader.entity.presence;

import java.time.OffsetDateTime;

/**
 * Unix timestamps for start and/or end of the game
 * 
 * @author Perry Berman
 */
public interface IActivityTimestamps {

	/**
	 * Returns an {@link OffsetDateTime} object representing the time at which the
	 * activity starts.
	 * 
	 * @return An {@link OffsetDateTime} object representing the time at which the
	 *         activity starts.
	 */
	OffsetDateTime getStartTime();

	/**
	 * Returns an {@link OffsetDateTime} object representing the time at which the
	 * activity ends.
	 * 
	 * @return An {@link OffsetDateTime} object representing the time at which the
	 *         activity ends.
	 */
	OffsetDateTime getEndTime();
}
