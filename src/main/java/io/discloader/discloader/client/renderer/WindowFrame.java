package io.discloader.discloader.client.renderer;

import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import io.discloader.discloader.client.logger.ProgressLogger;
import io.discloader.discloader.client.renderer.panel.FolderPanel;
import io.discloader.discloader.client.renderer.panel.LoadingPanel;
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
	public FolderPanel panel;
	
	public static final LoadingPanel loading = new LoadingPanel();
	
	protected ImageIcon createImageIcon(String path, String description) {
		URL imgURL = ClassLoader.getSystemResource(String.format("assets/discloader/%s.png", path.replace('.', '/')));
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public WindowFrame(DiscLoader loader) {
		this.loader = loader;
		this.setSize(960, 600);
		this.add(loading);
		this.setIconImage(createImageIcon("texture.gui.logos.floppy_disc", "icon").getImage());
		this.setTitle("DiscLoader");
		this.setBackground(new Color(0x2C2F33));
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
	
	public void postInit() {
		this.remove(loading);
		this.panel = new FolderPanel(this.loader);
		this.add(this.panel);
		this.revalidate();
		this.repaint();
	}
	
}
