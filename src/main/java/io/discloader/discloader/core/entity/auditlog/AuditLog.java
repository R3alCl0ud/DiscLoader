package io.discloader.discloader.core.entity.auditlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IWebhook;
import io.discloader.discloader.entity.auditlog.IAuditLog;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.AuditLogJSON;

public class AuditLog implements IAuditLog {

	private IGuild guild;
	private Map<Long, IUser> users;
	private Map<Long, IWebhook> webhooks;
	private Map<Long, IAuditLogEntry> entries;

	public AuditLog(IGuild guild, AuditLogJSON data) {
		users = new HashMap<>();
		webhooks = new HashMap<>();
		entries = new HashMap<>();
	}

	@Override
	public List<IAuditLogEntry> getEntries() {
		return new ArrayList<IAuditLogEntry>(entries.values());
	}

	@Override
	public IAuditLogEntry getEntry(long id) {
		return entries.get(id);
	}

	@Override
	public IGuild getGuild() {
		return guild;
	}

	@Override
	public DiscLoader getLoader() {
		return guild.getLoader();
	}

	@Override
	public Collection<IUser> getUsers() {
		return users.values();
	}

	@Override
	public Collection<IWebhook> getWebhooks() {
		return webhooks.values();
	}

}
