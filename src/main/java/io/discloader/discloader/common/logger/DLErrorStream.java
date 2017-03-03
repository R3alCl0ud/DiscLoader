package io.discloader.discloader.common.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class DLErrorStream extends PrintStream {

	private final Logger logger;

	public DLErrorStream(OutputStream out, Logger logger) {
		super(out);

		this.logger = logger;
	}

	public void print(String string) {
		this.logger.severe(string.trim().replaceAll("[\n\r]", "") + "\n");
	}

	public void println(String string) {
		this.logger.severe(string.trim());
	}

	public PrintStream printf(String template, Object... args) {
		for (Object o : args) {
			template = String.format(template, o);
		}
		this.logger.info(template);
		return (PrintStream) this.out;
	}

}
