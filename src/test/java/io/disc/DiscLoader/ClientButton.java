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
		this.setLocation(this.panel.getWidth(), (220 + this.panel.getHeight()));
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
			OpenPanel items  = new OpenPanel(new FlowLayout(FlowLayout.LEFT));
			JScrollPane scroll = new JScrollPane(items);
			if (this.data instanceof DiscLoader) {
				items.add(new ClientButton("user", items, ((DiscLoader) this.data).user));
				items.add(new ClientButton("users", items, ((DiscLoader) this.data).users));
				items.add(new ClientButton("guilds", items, ((DiscLoader) this.data).guilds));
			} else if (this.data instanceof User) {
				JLabel label = new JLabel("Username: " + ((User) this.data).username);
				label.setLocation(0, 0);
				contents.add(label);
				label = new JLabel("Discriminator: #" + ((User) this.data).discriminator);
				label.setLocation(0, 110);
				System.out.println(label.getHeight());
				contents.add(label);
			} else if (this.data instanceof HashMap<?, ?>) {
				((HashMap<?, ?>) this.data).forEach((t, item) -> {
					if (item instanceof User) {
						items.add(new ClientButton(((User) item).username, contents, item));
					}
				});
			}
			items.setSize(400, 720);
			scroll.setSize(400, 720);
			contents.add(scroll);
			contents.setSize(400, 720);
			contents.validate();
			this.panel.setActivePanel(contents);
			this.panel.revalidate();
			this.panel.repaint();
		}
	}

}
