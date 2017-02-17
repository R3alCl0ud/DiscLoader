/**
 * 
 */
package io.discloader.discloader.common.logger;

import java.io.IOException;

/**
 * @author Perry Berman
 *
 */
public interface ILogger {

	public String log(String log) throws IOException;
	
	public void shutdown() throws IOException;

}
