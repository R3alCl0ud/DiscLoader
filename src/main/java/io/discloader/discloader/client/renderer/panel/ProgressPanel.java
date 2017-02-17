package io.discloader.discloader.client.renderer.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Painter;
import javax.swing.UIDefaults;

/**
 * @author Perry Berman
 *
 */
public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -8832916168143980979L;

	public JProgressBar bar;

	public ProgressPanel(int min, int max) {
		super();
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.bar = new JProgressBar();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		Dimension minSpacing = new Dimension(200, 20), maxSpacing = new Dimension((int) Math.round(width/3) + 20, 20),
				perfSpacing = new Dimension(240, 20);
		this.bar.setMaximum(max);
		this.bar.setMinimum(min);
		this.bar.setStringPainted(true);
		this.bar.setMaximumSize(new Dimension(400, 80));
		this.bar.setMinimumSize(new Dimension(320, 70));
		this.bar.setSize(400, 40);
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
		this.add(this.bar, BorderLayout.CENTER);
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
		this.setSize(720, 150);
		this.validate();
	}

	public void setBarColor() {
		Color WHITE = new Color(255, 255, 255);
		Color RED = new Color(220, 0, 0);
		@SuppressWarnings("rawtypes")
		Painter p = new Painter() {

			@Override
			public void paint(Graphics2D g, Object object, int width, int height) {
				JProgressBar bar = (JProgressBar) object;
				g.setColor(bar.getBackground());
				g.fillRect(0, 0, width, height);
			}

		};
		// install custom painter on the bar
		UIDefaults properties = new UIDefaults();
		properties.put("ProgressBar[Enabled].backgroundPainter", p);
		this.bar.putClientProperty("Nimbus.Overrides", properties);

		this.bar.setBackground(WHITE);
		this.bar.setForeground(RED);
		// this.bar.revalidate();
		this.bar.repaint();
	}
}
