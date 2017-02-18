package io.discloader.discloader.client.renderer.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * @author Perry Berman
 *
 */
public class CenterPanel extends JPanel {

	private static final long serialVersionUID = 411346901958770653L;

	public CenterPanel(Component center) {
		super();
		this.setOpaque(true);
		this.setBackground(new Color(0, 0, 0, 0));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		Dimension minSpacing = new Dimension(0, 20),
				maxSpacing = new Dimension((int) ((width - center.getWidth()) / 2), 20),
				perfSpacing = new Dimension(20, 20);
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
		this.add(center, BorderLayout.CENTER);
		this.add(new Box.Filler(minSpacing, perfSpacing, maxSpacing));
	}

}
