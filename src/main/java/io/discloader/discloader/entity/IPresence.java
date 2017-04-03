package io.discloader.discloader.entity;

/**
 * @author Perry Berman
 */
public interface IPresence {

	String getStatus();

	IGame getGame();

	boolean equals(Object object);

}
