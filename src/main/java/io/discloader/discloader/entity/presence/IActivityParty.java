/**
 * 
 */
package io.discloader.discloader.entity.presence;

/**
 * The {@code size} of the party is an array of two integers
 * {@code [current_size, max_size]} used to show the party's current and maximum
 * size
 * 
 * @author Perry Berman
 */
public interface IActivityParty {
	int getCurrentSize();

	String getID();

	int getMaxSize();
}
