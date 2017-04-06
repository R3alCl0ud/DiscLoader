package io.discloader.discloader.client.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * @author Perry Berman
 */
public final class DLLogFormatter extends Formatter {

	private static final String PATTERN = "HH:mm:ss";

	@Override
	public String format(final LogRecord record) {
		String time = new SimpleDateFormat(PATTERN).format(new Date(record.getMillis()));

		return String.format("[%s] [%s/%s]: %s\n", time, record.getLoggerName(), record.getLevel().getName(), formatMessage(record).trim().trim());
	}

}
