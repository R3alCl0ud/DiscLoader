package io.discloader.discloader.core.entity.auditlog;

import io.discloader.discloader.entity.auditlog.IChangeValue;
import io.discloader.discloader.entity.util.SnowflakeUtil;

public class ChangeValue implements IChangeValue {

	private Object value;
	private String type;

	public ChangeValue(Object val, String type) {
		value = val;
		this.type = type;
	}

	// public boolean

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}

	@Override
	public int toInt() {
		return Integer.parseInt(toString(), 10);
	}

	@Override
	public long toLong() {
		return Long.parseLong(toString(), 10);
	}

	@Override
	public double toDouble() {
		return Double.parseDouble(toString());
	}

	@Override
	public float toFloat() {
		return Float.parseFloat(toString());
	}

	@Override
	public short toShort() {
		return Short.parseShort(toString(), 10);
	}

	@Override
	public boolean isBoolean() {
		return type.equalsIgnoreCase("bool");
	}

	@Override
	public boolean isDouble() {
		return type.equalsIgnoreCase("double");
	}

	@Override
	public boolean isFloat() {
		return type.equalsIgnoreCase("float");
	}

	@Override
	public boolean isInt() {
		return type.equalsIgnoreCase("int");
	}

	@Override
	public boolean isShort() {
		return type.equalsIgnoreCase("short");
	}

	@Override
	public boolean isString() {
		return type.equalsIgnoreCase("string");
	}

	@Override
	public boolean isLong() {
		return type.equalsIgnoreCase("long") || isSnowflake();
	}

	@Override
	public boolean isSnowflake() {
		return type.equalsIgnoreCase("snowflake");
	}

	@Override
	public boolean toBoolean() {
		if (value == null) return false;
		if (!isBoolean()) {
			if (isInt()) return toInt() != 0;
			if (isDouble()) return toDouble() != 0D;
			if (isFloat()) return toFloat() != 0F;
			if (isLong()) return toLong() != 0L;
			if (isShort()) return toShort() != 0;
			if (isString()) return !toString().isEmpty();
			if (isSnowflake()) return true;
		}
		return Boolean.parseBoolean(value.toString());
	}

	@Override
	public long toSnowflake() {
		return SnowflakeUtil.parse(toString());
	}

}
