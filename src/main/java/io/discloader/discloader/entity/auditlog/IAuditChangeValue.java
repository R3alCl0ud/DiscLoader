package io.discloader.discloader.entity.auditlog;

import io.discloader.discloader.entity.guild.IGuild;
import io.discloader.discloader.entity.guild.IRole;
import io.discloader.discloader.entity.user.IUser;

public interface IAuditChangeValue {

	String toString();
	
	int toInt();
	
	long toLong();
	
	double toDouble();
	
	float toFloat();
	
	short toShort();
}
