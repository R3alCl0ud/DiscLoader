package io.discloader.discloader.client.logger;

import io.discloader.discloader.client.render.panel.LoadingPanel;
import io.discloader.discloader.common.start.Main;

import java.util.logging.Logger;

/**
 * @author Perry Berman
 *
 */
public class ProgressLogger {

	public static int phaseNumber;
	public static Logger LOG = new DLLogger("Client Thread").getLogger();
	public static boolean loading = true;

	public static void phase(int value, int max, String text) {
		executeLog(text, 0);
		phaseNumber = value;
		if (!Main.usegui && loading)
			return;

		LoadingPanel.setPhase(value, max, text);
	}

	public static void stage(int value, int max, String text) {
		executeLog(text, 1);
		if (!Main.usegui && loading)
			return;

		LoadingPanel.setStage(value, max, text);
	}

	public static void step(int value, int max, String text) {
		executeLog(text, 2);
		if (!Main.usegui && loading)
			return;

		LoadingPanel.setStep(value, max, text);
	}

	public static void progress(int value, int max, String text) {
		executeLog(text, 3);
		if (!Main.usegui && loading)
			return;

		LoadingPanel.setProgress(value, max, text);
	}

	private static void executeLog(String text, int level) {
		switch (level) {
		case 0:
			LOG.info(String.format("%s", text));
			break;
		case 1:
			LOG.fine(String.format("%s", text));
			break;
		case 2:
			LOG.finer(String.format("%s", text));
			break;
		case 3:
			LOG.finest(String.format("%s", text));
			break;
		}
	}

}
