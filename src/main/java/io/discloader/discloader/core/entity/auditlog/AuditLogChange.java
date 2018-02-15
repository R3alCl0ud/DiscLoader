package io.discloader.discloader.core.entity.auditlog;

import java.util.Objects;

import io.discloader.discloader.entity.auditlog.AuditLogChangeKeys;
import io.discloader.discloader.entity.auditlog.IAuditLogChange;
import io.discloader.discloader.network.json.AuditLogChangeJSON;

public class AuditLogChange implements IAuditLogChange {

	private String key;
	private final Object newValue, oldValue;

	public AuditLogChange(AuditLogChangeJSON data) {
		key = data.key;
		newValue = data.new_value;
		oldValue = data.old_value;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AuditLogChange))
			return false;
		AuditLogChange other = (AuditLogChange) obj;
		return other.key.equals(key) && Objects.equals(other.oldValue, oldValue) && Objects.equals(other.newValue, newValue);
	}

	@Override
	public AuditLogChangeKeys getKey() {
		return AuditLogChangeKeys.getKey(key);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getNewValue() {
		return (T) newValue;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getOldValue() {
		return (T) oldValue;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, oldValue, newValue);
	}

	@Override
	public String toString() {
		return String.format("ALC:%s(%s -> %s)", key, oldValue, newValue);
	}
}
