package io.discloader.discloader.core.entity.auditlog;

import io.discloader.discloader.entity.auditlog.IAuditChangeValue;

public class AuditChangeValue implements IAuditChangeValue {

	private Object value;

	public AuditChangeValue(Object val) {
		value = val;
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int toInt() {
		return 0;
	}

	@Override
	public long toLong() {
		return 0;
	}

	@Override
	public double toDouble() {
		return 0;
	}

	@Override
	public float toFloat() {
		return 0;
	}

	@Override
	public short toShort() {
		return 0;
	}

}
