package io.discloader.discloader.client.render.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.net.URL;

import javax.swing.Box.Filler;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.discloader.discloader.client.logger.ProgressLogger;

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
		super();
		this.setOpaque(true);
		this.setBackground(new Color(0,0,0,0));
		Dimension min = new Dimension(0, 30), max = new Dimension(0, 150), pref = new Dimension(0, 75);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		ImageIcon discLoaderLogo = createImageIcon("texture.gui.logos.discloader","The DiscLoader Logo");
		JLabel discordLogo = new JLabel(createImageIcon("texture.gui.logos.discord_wordmark", "The Discord Logo"));
		JLabel poweredBy = new JLabel(createImageIcon("texture.gui.logos.powered_by", "Powered by Discord"));
		this.add(new Filler(min, pref, new Dimension(0, 200)));
		this.add(new CenterPanel(new JLabel(discLoaderLogo)));
//		this.add(new Filler(min, pref, max));
		this.add(new CenterPanel(poweredBy));
		this.add(new CenterPanel(discordLogo));
		this.add(new Filler(min, pref, max));
		this.add(phasePanel = new ProgressPanel(0, 2));
		this.add(stagePanel = new ProgressPanel(0, 1));
		this.add(stepPanel = new ProgressPanel(0, 1));
		this.add(progressPanel = new ProgressPanel(0, 1));
		ProgressLogger.phase(1, 3, "LOADING");
	}

	protected ImageIcon createImageIcon(String path, String description) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/discloader/%s.png", path.replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
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
