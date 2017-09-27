package io.discloader.discloader.entity.auditlog;

public interface IAuditLogChange {

	AuditLogChangeKeys getKey();
	
	IChangeValue getNewValue();
	IChangeValue getOldValue();
}
