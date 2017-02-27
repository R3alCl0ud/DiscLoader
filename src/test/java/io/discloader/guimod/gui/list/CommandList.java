package io.discloader.guimod.gui.list;

import javax.swing.DefaultListModel;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.CommandRegistry;

public class CommandList<T extends Command> extends AbstractList<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7432230130720564347L;

	public CommandList(DiscLoader loader) {
		super(loader);
	}

	@Override
	@SuppressWarnings("unchecked")
	public DefaultListModel<Object> createListModel() {
		DefaultListModel<Object> listModel = new DefaultListModel<Object>();
		for (Command command : CommandRegistry.commands.entries()) {
			listModel.addElement(String.format("<html>%s<br>%d</html>", command.getUnlocalizedName(), command.getId()));
			this.items.add((T) command);
		}
		return listModel;
	}
	
}
