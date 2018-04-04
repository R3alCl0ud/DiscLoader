package io.discloader.discloader.core.entity.auditlog;

import java.util.ArrayList;
import java.util.List;

import io.discloader.discloader.common.registry.EntityRegistry;
import io.discloader.discloader.entity.auditlog.ActionTypes;
import io.discloader.discloader.entity.auditlog.IAuditLog;
import io.discloader.discloader.entity.auditlog.IAuditLogChange;
import io.discloader.discloader.entity.auditlog.IAuditLogEntry;
import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.SnowflakeUtil;
import io.discloader.discloader.network.json.AuditLogChangeJSON;
import io.discloader.discloader.network.json.AuditLogEntryJSON;

public class AuditLogEntry implements IAuditLogEntry {

	private long id, user_id;
	private int actionType;
	private String reason, target_id;
	private List<IAuditLogChange> changes;
	private IAuditLog logs;

	public AuditLogEntry(IAuditLog logs, AuditLogEntryJSON data) {
		this.logs = logs;
		id = SnowflakeUtil.parse(data.id);
		user_id = SnowflakeUtil.parse(data.user_id);
		target_id = data.target_id;
		actionType = data.action_type;
		changes = new ArrayList<>();
		reason = data.reason;
		if (data.changes != null) {
			for (AuditLogChangeJSON cd : data.changes) {
				changes.add(new AuditLogChange(cd));
			}
		}
	}

	@Override
	public IAuditLog getAuditLogs() {
		return logs;
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
	public Object getTarget() {
		if (actionType == 1)
			return getAuditLogs().getGuild();
		else if (actionInRange(10, 15))
			return EntityRegistry.getChannelByID(target_id);
		else if (actionInRange(20, 23))
			return EntityRegistry.getUserByID(target_id);
		else if (actionInRange(24, 25))
			return getAuditLogs().getGuild().getMember(target_id);
		else if (actionInRange(30, 32))
			return getAuditLogs().getGuild().getRoleByID(target_id);
		else if (actionInRange(40, 42))
			return getAuditLogs().getGuild().getInvite(target_id);
		return null;
	}

	@Override
	public ActionTypes getActionType() {
		return ActionTypes.parseInt(actionType);
	}

	@Override
	public long getTargetID() {
		return SnowflakeUtil.parse(target_id);
	}

}
