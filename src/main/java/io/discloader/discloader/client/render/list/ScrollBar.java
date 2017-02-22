/**
 * 
 */
package io.discloader.discloader.client.render.list;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.plaf.basic.BasicScrollBarUI;

/**
 * @author Perry Berman
 *
 */
public class ScrollBar extends BasicScrollBarUI {

	public ScrollBar() {

	}

	protected void configureScrollBarColors() {
		this.thumbColor = new Color(0x7289DA);
//		this.decrButton.setVisible(false);
//		this.incrButton.setVisible(false);
	}
	
	protected JButton createDecreaseButton(int orientation) {
		this.decrButton = new JButton();
		this.decrButton.setVisible(false);
		this.decrButton.setPreferredSize(new Dimension(0,0));
		this.decrButton.setMinimumSize(new Dimension(0,0));
		this.decrButton.setMaximumSize(new Dimension(0,0));
		return this.decrButton;
	}
	
	protected JButton createIncreaseButton(int orientation) {
		this.incrButton = new JButton();
		this.incrButton.setVisible(false);
		this.incrButton.setPreferredSize(new Dimension(0,0));
		this.incrButton.setMinimumSize(new Dimension(0,0));
		this.incrButton.setMaximumSize(new Dimension(0,0));
		return this.incrButton;
	}
}
