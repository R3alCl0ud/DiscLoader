package io.discloader.discloader.entity;


public interface IOverwrite extends ISnowflake {

	/**
	 * @return
	 */
	int getAllowed();
	
	/**
	 * @return
	 */
	int getDenied();
	
}
