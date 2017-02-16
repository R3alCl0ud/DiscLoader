package io.discloader.discloader.client.renderer.panel;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Perry Berman
 *
 */
public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -8832916168143980979L;

	public final JProgressBar bar;
	
	public ProgressPanel(int min, int max) {
		super(new GridLayout(0, 1));
		this.add(this.bar = new JProgressBar());
		this.bar.setMaximum(max);
		this.bar.setMinimum(min);
		this.bar.setStringPainted(true);
		this.setSize(200, 15);
		this.validate();
	}
}
