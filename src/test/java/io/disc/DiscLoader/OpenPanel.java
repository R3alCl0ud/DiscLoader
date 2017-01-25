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
	}
	
	public OpenPanel() {
		super();
	}

	public Component setActiveComponent(Component comp) {
		this.remove(this.active);
		this.active = comp;
		this.add(this.active);
		return comp;
	}

}
