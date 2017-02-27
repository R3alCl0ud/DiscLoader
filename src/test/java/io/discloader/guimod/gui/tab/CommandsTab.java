package io.discloader.guimod.gui.tab;

import javax.swing.event.ListSelectionEvent;

import io.discloader.discloader.client.command.Command;
import io.discloader.discloader.common.DiscLoader;
import io.discloader.guimod.gui.info.CommandInfo;
import io.discloader.guimod.gui.list.CommandList;

public class CommandsTab<T extends Command> extends AbstractFolder<T, CommandList<T>, CommandInfo<T>> {

	private static final long serialVersionUID = -2269526589617580218L;

	public CommandsTab(DiscLoader loader) {
		super(loader);
	}
	
	@Override
	public CommandList<T> createList() {
		return new CommandList<T>(this.loader);
	}

	@Override
	public CommandInfo<T> createInfo() {
		return new CommandInfo<T>();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (this.list.folders.getSelectedIndex() != -1) {
			this.info.update(this.list.items.get(list.folders.getSelectedIndex()));
		}
	}
	
}

