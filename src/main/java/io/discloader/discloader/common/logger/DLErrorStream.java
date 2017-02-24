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

}
