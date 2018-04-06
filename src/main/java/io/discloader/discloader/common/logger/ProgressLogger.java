package io.discloader.discloader.common.logger;

import io.discloader.discloader.common.start.Main;

/**
 * @author Perry Berman
 */
public class ProgressLogger {

	public static int phaseNumber;
	public static boolean loading = true;

	public static void phase(int value, int max, String text) {
		// executeLog(text, 0);
		phaseNumber = value;
	}

	public static void stage(int value, int max, String text) {
		// executeLog(text, 1);
		if (!Main.usegui && loading) return;
	}

	public static void step(int value, int max, String text) {
		// executeLog(text, 2);
	}

	public static void progress(int value, int max, String text) {
		// executeLog(text, 3);
	}

}
