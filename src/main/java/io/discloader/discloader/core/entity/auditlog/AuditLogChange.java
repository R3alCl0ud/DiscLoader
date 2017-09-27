package io.discloader.discloader.core.entity.auditlog;

import io.discloader.discloader.entity.auditlog.IChangeValue;
import io.discloader.discloader.entity.auditlog.AuditLogChangeKeys;
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
	public IChangeValue getNewValue() {
		return new ChangeValue(newValue, getKey().getType());
	}

	@Override
	public IChangeValue getOldValue() {
		return new ChangeValue(oldValue, getKey().getType());
	}

	@Override
	public AuditLogChangeKeys getKey() {
		return AuditLogChangeKeys.getKey(key);
	}

}
