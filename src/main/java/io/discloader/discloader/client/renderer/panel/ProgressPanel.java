package io.discloader.discloader.client.renderer.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;

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
	private JPanel left;
	private JPanel right;

	public ProgressPanel(int min, int max) {
		super(new BorderLayout());
		this.left = new JPanel();
		this.bar = new JProgressBar();
		this.right = new JPanel();
		Dimension spacing = new Dimension(100, 200);
		this.left.setMinimumSize(spacing);
		this.right.setMinimumSize(spacing);
		this.bar.setMaximum(max);
		this.bar.setMinimum(min);
		this.bar.setStringPainted(true);
		this.bar.setMaximumSize(new Dimension(400, 80));
		this.bar.setMinimumSize(new Dimension(400, 70));
		this.bar.setSize(400, 40);
		this.add(this.left, BorderLayout.WEST);
		this.add(this.bar, BorderLayout.CENTER);
		this.add(this.right, BorderLayout.EAST);
		this.setSize(720, 150);
		this.validate();
		System.out.println(this.getSize().getWidth());
	}

	public void setBarColor() {
		Color color = new Color(255, 20, 20);
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

		this.bar.setBackground(color);
//		this.bar.revalidate();
		this.bar.repaint();
	}
}
