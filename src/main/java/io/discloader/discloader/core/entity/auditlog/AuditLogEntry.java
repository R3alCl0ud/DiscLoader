package io.discloader.discloader.core.entity.auditlog;

import java.util.ArrayList;
import java.util.List;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.auditlog.IAuditLogChange;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.AuditLogChangeJSON;
import io.discloader.discloader.network.json.AuditLogEntryJSON;

public class AuditLogEntry implements IAuditLogEntry {

	private long id, target_id, user_id;
	private int actionType;
	private String reason;
	private List<IAuditLogChange> changes;

	public AuditLogEntry(AuditLogEntryJSON data) {
		id = SnowflakeUtil.parse(data.id);
		user_id = SnowflakeUtil.parse(data.user_id);
		target_id = SnowflakeUtil.parse(data.target_id);
		actionType = data.action_type;
		changes = new ArrayList<>();
		for (AuditLogChangeJSON cd : data.changes) {
			changes.add(new AuditLogChange(cd));
		}
	}

	@Override
	public IUser getAuthor() {
		return EntityRegistry.getUserByID(user_id);
	}

	@Override
	public List<IAuditLogChange> getChanges() {
		return changes;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getReason() {
		return reason;
	}

	@Override
	public ISnowflake getTarget() {
		if (actionType == 1) return EntityRegistry.getGuildByID(target_id);
		if (actionType >= 10 && actionType <= 15) return EntityRegistry.getChannelByID(target_id);
		return null;
	}

}
