package io.discloader.discloader.client.renderer.panel;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * @author Perry Berman
 *
 */
public class LoadingPanel extends JPanel {

	private static final long serialVersionUID = 704573790954978688L;
	public static ProgressPanel phasePanel;
	public static ProgressPanel stagePanel;
	public static ProgressPanel stepPanel;
	public static ProgressPanel progressPanel;

	public LoadingPanel() {
		super(new GridLayout(0, 1));
		this.add(phasePanel = new ProgressPanel(0, 2));
		this.add(stagePanel = new ProgressPanel(0, 1));
		this.add(stepPanel = new ProgressPanel(0, 1));
		this.add(progressPanel = new ProgressPanel(0, 1));
		setPhase(1, 2, "LOADING");
//		start.loader.modh.beginLoader();
	}

	public static void setPhase(int phase, int maxPhase, String text) {
		phasePanel.bar.setMaximum(maxPhase);
		phasePanel.bar.setValue(phase);
		phasePanel.bar.setString(String.format("%d/%d: %s", phase, maxPhase, text));
		phasePanel.revalidate();
	}

	public static void setStage(int stage, int maxStage, String text) {
		stagePanel.bar.setMaximum(maxStage);
		stagePanel.bar.setValue(stage);
		stagePanel.bar.setString(String.format("%d/%d: %s", stage, maxStage, text));
		stagePanel.revalidate();
	}

	public static void setStep(int step, int maxStep, String text) {
		stepPanel.bar.setMaximum(maxStep);
		stepPanel.bar.setValue(step);
		stepPanel.bar.setString(String.format("%d/%d: %s", step, maxStep, text));
		stepPanel.revalidate();
	}

	public static void setProgress(int progress, int maxProgress, String text) {
		progressPanel.bar.setMaximum(maxProgress);
		progressPanel.bar.setValue(progress);
		progressPanel.bar.setString(String.format("%d/%d: %s", progress, maxProgress, text));
		progressPanel.revalidate();
	}
}
