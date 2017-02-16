package io.discloader.discloader.client.renderer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.*;

import io.discloader.discloader.client.logging.ProgressLogger;
import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.client.renderer.panel.WindowPanel;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.discovery.ModCandidate;
import io.discloader.discloader.common.discovery.ModDiscoverer;
import io.discloader.discloader.common.registry.ModRegistry;

/**
 * @author Perry Berman
 *
 */
public class WindowFrame extends JFrame {
	private static final long serialVersionUID = -6329873205165995031L;
	public DiscLoader loader;
	public WindowPanel panel;
	public static final LoadingPanel loading = new LoadingPanel();

	public WindowFrame(DiscLoader loader) {
		this.loader = loader;
		this.panel = new WindowPanel(this.loader);
		this.setSize(720, 600);
		this.add(loading);
		this.setTitle("DiscLoader");
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowListener() {

			@Override
			public void windowActivated(WindowEvent arg0) {

			}

			@Override
			public void windowClosed(WindowEvent arg0) {

			}

			@Override
			public void windowClosing(WindowEvent arg0) {

			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {

			}

			@Override
			public void windowIconified(WindowEvent arg0) {

			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				LoadingPanel.phasePanel.setBarColor();
				LoadingPanel.stagePanel.setBarColor();
				LoadingPanel.stepPanel.setBarColor();
				LoadingPanel.progressPanel.setBarColor();
				loading.repaint();
				TimerTask giveItTime = new TimerTask() {

					@Override
					public void run() {
						ProgressLogger.stage(1, 3, "Mod Discovery");
						ModDiscoverer.checkModDir();
						ArrayList<ModCandidate> candidates = ModDiscoverer.discoverMods();
						TimerTask checkCandidates = new TimerTask() {

							@Override
							public void run() {
								ProgressLogger.stage(2, 3, "Discovering Mod Containers");
								ModRegistry.checkCandidates(candidates);
							}
							
						};
						loader.timer.schedule(checkCandidates, 500);
					}
				};
				loader.timer.schedule(giveItTime, 3000);
			}

		});
	}
}
