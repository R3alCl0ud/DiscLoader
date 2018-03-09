package io.discloader.discloader.entity.auditlog;

import java.util.List;

import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IAuditLogEntry extends ISnowflake {

	IAuditLog getAuditLogs();

	IUser getAuthor();

	List<IAuditLogChange> getChanges();

	String getReason();

	Object getTarget();

	ActionTypes getActionType();
	
	long getTargetID();

	default boolean actionInRange(int min, int max) {
		int at = getActionType().toInt();
		return at >= min && at <= max;
	}
}
