package io.discloader.discloader.core.entity.auditlog;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.auditlog.IAuditLogChange;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.AuditLogEntryJSON;

public class AuditLogEntry implements IAuditLogEntry {

	private long id, target_id, user_id;
	private String reason;
	private IAuditLogChange change;

	public AuditLogEntry(AuditLogEntryJSON data) {
		id = SnowflakeUtil.parse(data.id);
		user_id = SnowflakeUtil.parse(data.user_id);
		target_id = SnowflakeUtil.parse(data.target_id);
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public ISnowflake getTarget() {
		return null;
	}

	@Override
	public IUser getAuthor() {
		return EntityRegistry.getUserByID(user_id);
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public IAuditLogChange getChange() {
		return null;
	}

}
