/**
 * 
 */
package io.discloader.discloader.client.logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Perry Berman
 */
public class DLLogger {

	public final Logger LOGGER;
	private static FileHandler fileHandler = null;
	private static File logFolder = new File("logs");

	@Deprecated
	public DLLogger(Class<?> cls) {
		this(cls.getSimpleName());
	}

	@Deprecated
	public DLLogger(String name) {

		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}

		this.LOGGER = Logger.getLogger(name);
		if (LOGGER.getHandlers().length < 2) {
			Handler consoleHandler = new Handler() {

				@Override
				public void publish(LogRecord record) {
					if (getFormatter() == null) {
						setFormatter(new DLLogFormatter());
					}

					try {
						String message = getFormatter().format(record);
						if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
							System.err.write(message.getBytes());
						} else {
							System.out.write(message.getBytes());
						}
					} catch (Exception exception) {
						reportError(null, exception, ErrorManager.FORMAT_FAILURE);
					}

				}

				@Override
				public void close() throws SecurityException {}

				@Override
				public void flush() {}
			};
			this.LOGGER.setLevel(Level.ALL);
			this.LOGGER.addHandler(consoleHandler);
			this.LOGGER.addHandler(getFileHandler());
			this.LOGGER.setUseParentHandlers(false);
		}
	}

	public Logger getLogger() {
		return this.LOGGER;
	}

	public static Logger getLogger(Object obj) {
		return getLogger(obj.toString());
	}

	public static Logger getLogger(Class<?> cls) {
		return getLogger(cls.getSimpleName());
	}

	public static Logger getLogger(String name) {
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}

		Logger logger = Logger.getLogger(name);
		if (logger.getHandlers().length < 2) {
			Handler consoleHandler = new Handler() {

				@Override
				public void publish(LogRecord record) {
					if (getFormatter() == null) {
						setFormatter(new DLLogFormatter());
					}

					try {
						String message = getFormatter().format(record);
						if (record.getLevel().intValue() >= Level.WARNING.intValue()) {
							System.err.write(message.getBytes());
						} else {
							System.out.write(message.getBytes());
						}
					} catch (Exception exception) {
						reportError(null, exception, ErrorManager.FORMAT_FAILURE);
					}

				}

				@Override
				public void close() throws SecurityException {}

				@Override
				public void flush() {}
			};
			logger.setLevel(Level.ALL);
			logger.addHandler(consoleHandler);
			logger.addHandler(getFileHandler());
			logger.setUseParentHandlers(false);
		}
		return logger;
	}

	public static Logger getLogger(String name, String resourceBundleName) {
		if (!logFolder.exists()) {
			logFolder.mkdirs();
		}
		Logger logger = Logger.getLogger(name, resourceBundleName);

		return logger;
	}

	/**
	 * @return the fileHandler
	 */
	public static FileHandler getFileHandler() {
		if (fileHandler == null) {
			try {
				Date date = new Date();
				date.setTime(System.currentTimeMillis());
				fileHandler = new FileHandler(String.format("logs/%s.log", date.toString().replace(':', '-')));
				fileHandler.setFormatter(new DLLogFormatter());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return fileHandler;
	}
}
