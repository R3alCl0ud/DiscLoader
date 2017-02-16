package io.discloader.discloader.client.logging;

import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.common.start.Start;

/**
 * @author Perry Berman
 *
 */
public class ProgressLogger {

	public static void phase(int value, int max, String text) {
		if (Start.nogui)
			return;
		
		LoadingPanel.setPhase(value, max, text);
	}
	
	public static void stage(int value, int max, String text) {
		if (Start.nogui)
			return;
		
		LoadingPanel.setStage(value, max, text);
	}
	
	public static void step(int value, int max, String text) {
		if (Start.nogui)
			return;
		
		LoadingPanel.setStep(value, max, text);
	}
	
	public static void progress(int value, int max, String text) {
		if (Start.nogui)
			return;
		
		LoadingPanel.setProgress(value, max, text);
	}

}
