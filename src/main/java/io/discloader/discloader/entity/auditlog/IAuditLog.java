package io.discloader.discloader.entity.auditlog;

import java.util.List;
import java.util.Map;

import io.discloader.discloader.common.DiscLoader;
import io.discloader.discloader.entity.IWebhook;
import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.user.IUser;

public interface IAuditLog {

	List<IAuditLogEntry> getEntries();

	IAuditLogEntry getEntry(long id);

	IGuild getGuild();

	DiscLoader getLoader();

	// Collection<IUser> getUsers();

	Map<Long, IUser> getUsers();

	Map<Long, IWebhook> getWebhooks();

}
