package net.vicp.dgiant.entry.common;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="user_role")
public class UserRole {
	
	@DatabaseField(generatedId = true)
	private int id;

	@DatabaseField(foreign = true, columnName = "user_id")
	private User user;

	@DatabaseField(foreign = true, columnName = "role_id")
	private Role role;

	public UserRole() {
		// for ormlite
	}

	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}
}
