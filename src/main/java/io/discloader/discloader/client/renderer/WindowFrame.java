package io.discloader.discloader.client.renderer;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.TimerTask;

import javax.swing.*;

import io.discloader.discloader.client.renderer.panel.LoadingPanel;
import io.discloader.discloader.client.renderer.panel.WindowPanel;
import io.discloader.discloader.common.DiscLoader;

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
				TimerTask giveItTime = new TimerTask() {

					@Override
					public void run() {
						
					}
				};
				loader.timer.schedule(giveItTime, 3000);
			}

		});
	}
}
