package io.disc.DiscLoader.objects.window;

import javax.swing.*;

import io.disc.DiscLoader.DiscLoader;

/**
 * @author Perry Berman
 *
 */
public class WindowFrame extends JFrame {
	public DiscLoader loader;
	public WindowPanel panel;
	public WindowFrame(DiscLoader loader) {
		this.loader = loader;
		this.panel = new WindowPanel(this.loader);
		this.setSize(720, 600);
		this.add(this.panel);
		this.setTitle("DiscLoader");
		
//		this.pack();
		this.setVisible(true);
	}

}
