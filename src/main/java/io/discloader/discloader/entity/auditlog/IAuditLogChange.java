package io.discloader.discloader.entity.auditlog;

public interface IAuditLogChange {

	String getKey();
	
	IAuditChangeValue getNewValue();
	IAuditChangeValue getOldValue();
}
