package io.discloader.discloader.core.entity.auditlog;

import io.discloader.discloader.entity.auditlog.IAuditChangeValue;
import io.discloader.discloader.entity.auditlog.IAuditLogChange;
import io.discloader.discloader.network.json.AuditLogChangeJSON;

public class AuditLogChange implements IAuditLogChange {

	private String key;
	private Object newValue, oldValue;

	public AuditLogChange(AuditLogChangeJSON data) {
		key = data.key;
		newValue = data.new_value;
		oldValue = data.old_value;
	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public IAuditChangeValue getNewValue() {
		return null;
	}

	@Override
	public IAuditChangeValue getOldValue() {
		return null;
	}

}
