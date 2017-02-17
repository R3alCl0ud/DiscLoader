package io.discloader.discloader.client.logger;

import java.io.IOException;

import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.common.logger.FileLogger;
import io.discloader.discloader.common.start.Main;

/**
 * @author Perry Berman
 *
 */
public class ProgressLogger {

	public static int phaseNumber;
	private static FileLogger LOG = Main.getLOGGER();

	public static void phase(int value, int max, String text) {
		executeLog("phase", value, max, text);
		phaseNumber = value;
		if (Main.nogui)
			return;

		LoadingPanel.setPhase(value, max, text);
	}

	public static void stage(int value, int max, String text) {
		executeLog("stage", value, max, text);
		if (Main.nogui)
			return;

		LoadingPanel.setStage(value, max, text);
	}

	public static void step(int value, int max, String text) {
		executeLog("step", value, max, text);
		if (Main.nogui)
			return;

		LoadingPanel.setStep(value, max, text);
	}

	public static void progress(int value, int max, String text) {
		executeLog("progress", value, max, text);
		if (Main.nogui)
			return;

		LoadingPanel.setProgress(value, max, text);
	}

	private static void executeLog(String log, int value, int max, String text) {
		try {
			LOG.log(String.format("%s: %d/%d: %s", log, value, max, text));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
