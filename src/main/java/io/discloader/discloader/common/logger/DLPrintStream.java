package io.discloader.discloader.common.logger;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.logging.Logger;

public class DLPrintStream extends PrintStream {

	private final Logger logger;

	public DLPrintStream(OutputStream out, Logger logger) {
		super(out, true);

		this.logger = logger;
	}

	public void print(Object object) {
		logger.info(object.toString());
	}

	public void print(int integer) {
		this.logger.info(Integer.toString(integer));
	}

	public void print(String string) {
		this.logger.info(string.trim());
	}

	public PrintStream printf(String template, Object... args) {
		for (Object o : args) {
			template = String.format(template, o);
		}
		this.logger.info(template);
		return (PrintStream) this.out;
	}

	public void println(int integer) {
		this.logger.info(Integer.toString(integer));
	}

	public void println(String string) {
		this.logger.info(string);
	}

}
