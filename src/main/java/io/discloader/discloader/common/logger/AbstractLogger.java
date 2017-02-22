package io.discloader.discloader.common.logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;
import java.time.Instant;

/**
 * @author Perry Berman
 *
 */
public abstract class AbstractLogger implements ILogger {

	protected File logDir;
	
	public AbstractLogger(String name, String resourceBundleName) {
//		super(name, resourceBundleName);
		logDir = new File("logs");
		if (!logDir.exists() || !logDir.isDirectory()) {
			logDir.mkdir();
		}
	}

	@Override
	public String log(String text) throws IOException {
		Date time = Date.from(Instant.now());
		long days = Math.round(time.getTime() / 86400000);
		String log = String.format("[%d:%d:%d:%d] %s\n", days, time.getHours(), time.getMinutes(), time.getSeconds(), text);
		System.out.printf(log);
		return log;
	}
}
