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

	public void print(Object object) {
		logger.severe(object.toString().trim());
	}

	public void print(int integer) {
		logger.severe(Integer.toString(integer).trim());
	}

	public void print(char character) {
		logger.severe(Character.toString(character).trim());
	}

	public void print(byte bite) {
		logger.severe(Byte.toString(bite).trim());
	}

	public void print(short small) {
		logger.severe(Short.toString(small).trim());
	}

	public void print(float floating) {
		logger.severe(Float.toString(floating).trim());
	}

	public void print(double decimal) {
		logger.severe(Double.toString(decimal).trim());
	}

	public void print(long int64) {
		logger.severe(Long.toString(int64, 10).trim());
	}

	public void print(boolean bool) {
		logger.severe(Boolean.toString(bool).trim());
	}

	public void print(String string) {
		logger.severe(string.trim());
	}

	public PrintStream printf(String template, Object... args) {
		for (String l : String.format(template, args).split("\n"))
			logger.severe(l);
		return (PrintStream) out;
	}

	public void println(int integer) {
		logger.severe(Integer.toString(integer));
	}

	public void println(char character) {
		logger.severe(Character.toString(character));
	}

	public void println(byte bite) {
		logger.severe(Byte.toString(bite));
	}

	public void println(short small) {
		logger.severe(Short.toString(small));
	}

	public void println(float f) {
		logger.severe(Float.toString(f));
	}

	public void println(double d) {
		logger.severe(Double.toString(d));
	}

	public void println(long int64) {
		logger.severe(Long.toString(int64, 10));
	}

	public void println(boolean bool) {
		logger.severe(Boolean.toString(bool));
	}

	public void println(String string) {
		logger.severe(string);
	}

	public void println(Object object) {
		logger.severe(object.toString());
	}

}
