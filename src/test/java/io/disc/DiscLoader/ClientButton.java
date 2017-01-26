/**
 * 
 */
package io.disc.DiscLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.util.HashMap;

import javax.swing.*;
import javax.swing.event.*;

import io.disc.DiscLoader.objects.structures.User;

/**
 * @author Perry Berman
 *
 */
@SuppressWarnings("serial")
public class ClientButton extends JButton implements ActionListener {

	
	public boolean open;
	public OpenPanel panel;
	public Object data;
	
	public ClientButton(String content, OpenPanel panel, Object data) {
		super(content);
		
		this.panel = panel;
		this.panel.applyComponentOrientation(ComponentOrientation.getOrientation(this.panel.getLocale()));
		this.setLocation(0, (220 + this.panel.getHeight()));
		this.addActionListener(this);
		this.open = false;
		this.data = data;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.open = !this.open;
		System.out.println("open: " + this.open);
		if (this.open) {
			OpenPanel contents = new OpenPanel(new FlowLayout(FlowLayout.LEFT));
			if (this.data instanceof DiscLoader) {
				contents.add(new ClientButton("user", contents, ((DiscLoader) this.data).user));
				contents.add(new ClientButton("users", contents, ((DiscLoader) this.data).users));
				contents.add(new ClientButton("guilds", contents, ((DiscLoader) this.data).guilds));
			} else if (this.data instanceof User) {
				JLabel label = new JLabel("Username: " + ((User) this.data).username);
				label.setLocation(0, 0);
				contents.add(label);
				label = new JLabel("Discriminator: #" + ((User) this.data).discriminator);
				label.setLocation(0, 110 + contents.getHeight());
				System.out.println(label.getWidth());
				contents.add(label);
			} else if (this.data instanceof HashMap<?, ?>) {
				((HashMap<?, ?>) this.data).forEach((t ,item) -> {
					if (item instanceof User) {
						contents.add(new ClientButton(((User)item).username, contents, item));
					}
				});
			}
			contents.validate();
			this.panel.setActivePanel(contents);
			this.panel.revalidate();
			this.panel.repaint();
		}
	}

}
