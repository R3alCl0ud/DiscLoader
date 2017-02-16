package io.discloader.discloader.client.logging;

import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.common.start.Start;

/**
 * @author Perry Berman
 *
 */
public class ProgressLogger {

	public static int phaseNumber;
	
	public static void phase(int value, int max, String text) {
		printLog("phase", value, max, text);
		phaseNumber = value;
		if (Start.nogui)
			return;

		LoadingPanel.setPhase(value, max, text);
//		stage(0, 0, "");
	}

	public static void stage(int value, int max, String text) {
		printLog("stage", value, max, text);
		if (Start.nogui)
			return;

		LoadingPanel.setStage(value, max, text);
//		step(0, 0, "");
	}

	public static void step(int value, int max, String text) {
		printLog("step", value, max, text);
		if (Start.nogui)
			return;

		LoadingPanel.setStep(value, max, text);
//		progress(0, 0, "");
	}

	public static void progress(int value, int max, String text) {
		printLog("progress", value, max, text);
		if (Start.nogui)
			return;

		LoadingPanel.setProgress(value, max, text);
	}

	private static void printLog(String log,int value, int max, String text) {
		System.out.println(String.format("%s: %d/%d: %s", log, value, max, text));
	}
	
}
