package io.discloader.discloader.entity.auditlog;

import java.util.List;

import io.discloader.discloader.entity.user.IUser;
import io.discloader.discloader.entity.util.ISnowflake;

public interface IAuditLogEntry extends ISnowflake {

	String getReason();

	ISnowflake getTarget();

	IUser getAuthor();

	List<IAuditLogChange> getChanges();
}
