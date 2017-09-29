package io.discloader.discloader.core.entity.auditlog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.IWebhook;
import io.discloader.discloader.entity.auditlog.IAuditLog;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.network.json.AuditLogEntryJSON;
import io.discloader.discloader.network.json.AuditLogJSON;
import io.discloader.discloader.network.json.UserJSON;
import io.discloader.discloader.network.json.WebhookJSON;

public class AuditLog implements IAuditLog {

	private IGuild guild;
	private Map<Long, IUser> users;
	private Map<Long, IWebhook> webhooks;
	private Map<Long, IAuditLogEntry> entries;

	public AuditLog(IGuild guild, AuditLogJSON data) {
		this.guild = guild;
		users = new HashMap<>();
		webhooks = new HashMap<>();
		entries = new HashMap<>();
		for (UserJSON ud : data.users) {
			IUser user = EntityRegistry.addUser(ud);
			users.put(user.getID(), user);
		}

		for (WebhookJSON wd : data.webhooks) {
			IWebhook wh = EntityRegistry.addWebhook(wd);
			webhooks.put(wh.getID(), wh);
		}

		for (AuditLogEntryJSON ed : data.audit_log_entries) {
			IAuditLogEntry en = new AuditLogEntry(this, ed);
			entries.put(en.getID(), en);
		}
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
