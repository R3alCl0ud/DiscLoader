package io.discloader.discloader.network.json;

public class AuditLogEntryJSON {

	public String target_id, user_id, id, reason;
	public int action_type;
	public AuditOptionsJSON options;
	public AuditLogChangeJSON[] changes;
}
