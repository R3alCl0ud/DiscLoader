package io.discloader.discloader.objects.window;

import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import io.discloader.discloader.main.start;

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
		this.add(stagePanel = new ProgressPanel(0, 0));
		this.add(stepPanel = new ProgressPanel(0, 0));
		this.add(progressPanel = new  ProgressPanel(0,0)); 
		setPhase(1, 2, "Begin Loader");
		start.loader.modh.beginLoader();
	}

	public static void setPhase(int phase, int maxPhase, String text) {
		phasePanel.bar.setMaximum(maxPhase);
		phasePanel.bar.setValue(phase);
		phasePanel.bar.setString(String.format("[%d:%d]: %s", phase, maxPhase, text));
	}
	
	public static void setStage(int stage, int maxStage, String text) {
		stagePanel.bar.setMaximum(maxStage);
		stagePanel.bar.setValue(stage);
		stagePanel.bar.setString(String.format("[%d:%d]: %s", stage, maxStage, text));
	}

	public static void setStep(int step, int maxStep, String text) {
		stepPanel.bar.setMaximum(maxStep);
		stepPanel.bar.setValue(step);
		stepPanel.bar.setString(String.format("[%d:%d]: %s", step, maxStep, text));
	}

	public static void setProgess(int progress, int maxProgress, String text) {
		progressPanel.bar.setMaximum(maxProgress);
		progressPanel.bar.setValue(progress);
		progressPanel.bar.setString(String.format("[%d:%d]: %s", progress, maxProgress, text));
	}
}
