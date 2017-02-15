package io.discloader.discloader.objects.window;

import javax.swing.*;

import io.discloader.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class WindowFrame extends JFrame {
	private static final long serialVersionUID = -6329873205165995031L;
	public DiscLoader loader;
	public WindowPanel panel;
	public LoadingPanel loading;

	public WindowFrame(DiscLoader loader) {
		this.loader = loader;
		this.panel = new WindowPanel(this.loader);
		this.loading = new LoadingPanel();
		this.setSize(720, 600);
		this.add(this.loading);
		this.setTitle("DiscLoader");
		this.setVisible(true);
	}
	
	

}
