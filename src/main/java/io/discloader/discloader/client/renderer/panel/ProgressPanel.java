package io.discloader.discloader.client.renderer.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Painter;
import javax.swing.UIDefaults;
import javax.swing.border.Border;

/**
 * @author Perry Berman
 *
 */
public class ProgressPanel extends JPanel {

	private static final long serialVersionUID = -8832916168143980979L;

	public JProgressBar bar;

	private Border border;
	
	public ProgressPanel(int min, int max) {
		super();
		this.setOpaque(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.bar = new JProgressBar();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		Dimension minSpacing = new Dimension(0, 30), maxSpacing = new Dimension((int) ((width - 550) / 2), 30),
				perfSpacing = new Dimension(20, 30);
		this.bar.setMaximum(max);
		this.bar.setMinimum(min);
		this.bar.setStringPainted(true);
		this.bar.setMaximumSize(new Dimension(550, 20));
		this.bar.setMinimumSize(new Dimension(550, 10));
		this.bar.setPreferredSize(new Dimension(550, 15));
		this.bar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
		this.add(this.bar, BorderLayout.CENTER);
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
		this.setSize(720, 30);
		this.validate();
	}

	public void setBarColor() {
		Color WHITE = new Color(255, 255, 255);
		Color RED = new Color(0x7289DA);
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
