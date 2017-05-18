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
		logger.info(object.toString().trim());
	}

	public void print(int integer) {
		logger.info(Integer.toString(integer).trim());
	}

	public void print(char character) {
		logger.info(Character.toString(character).trim());
	}

	public void print(byte bite) {
		logger.info(Byte.toString(bite).trim());
	}

	public void print(short small) {
		logger.info(Short.toString(small).trim());
	}

	public void print(float floating) {
		logger.info(Float.toString(floating).trim());
	}

	public void print(double decimal) {
		logger.info(Double.toString(decimal).trim());
	}

	public void print(long int64) {
		logger.info(Long.toString(int64, 10).trim());
	}

	public void print(boolean bool) {
		logger.info(Boolean.toString(bool).trim());
	}

	public void print(String string) {
		logger.info(string.trim());
	}

	public PrintStream printf(String template, Object... args) {
		for (String l : String.format(template, args).split("\n"))
			logger.info(l);
		return (PrintStream) out;
	}

	public void println(int integer) {
		logger.info(Integer.toString(integer));
	}

	public void println(char character) {
		logger.info(Character.toString(character));
	}

	public void println(byte bite) {
		logger.info(Byte.toString(bite));
	}

	public void println(short small) {
		logger.info(Short.toString(small));
	}

	public void println(float f) {
		logger.info(Float.toString(f));
	}

	public void println(double d) {
		logger.info(Double.toString(d));
	}

	public void println(long int64) {
		logger.info(Long.toString(int64, 10));
	}

	public void println(boolean bool) {
		logger.info(Boolean.toString(bool));
	}

	public void println(String string) {
		logger.info(string);
	}

	public void println(Object object) {
		logger.info(object.toString());
	}

}
