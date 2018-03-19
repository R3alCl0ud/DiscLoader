package io.discloader.discloader.entity.presence;

/**
 * @author Perry Berman
 */
public interface IPresence {

	String getStatus();

	@Deprecated
	IActivity getGame();

	IActivity getActivity();

	boolean equals(Object object);

}
