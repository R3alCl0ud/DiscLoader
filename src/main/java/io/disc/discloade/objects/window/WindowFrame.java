package io.disc.discloader.objects.window;

import javax.swing.*;

import io.disc.discloader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class WindowFrame extends JFrame {
	private static final long serialVersionUID = -6329873205165995031L;
	public DiscLoader loader;
	public WindowPanel panel;

	public WindowFrame(DiscLoader loader) {
		this.loader = loader;
		this.panel = new WindowPanel(this.loader);
		this.setSize(720, 600);
		this.add(this.panel);
		this.setTitle("DiscLoader");
		this.setVisible(true);
	}

}
