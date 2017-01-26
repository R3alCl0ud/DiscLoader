package io.disc.DiscLoader;

import javax.swing.*;
import java.awt.*;


public class OpenPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Component active;

	public OpenPanel(FlowLayout flowLayout) {
		super(flowLayout);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	}
	
	public OpenPanel() {
		super();
	}

	public Component setActivePanel(Component panel) {
		if (this.active != null) this.remove(this.active);
		this.active = panel;
		this.add(this.active);
		return panel;
	}

}
