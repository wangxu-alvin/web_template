package net.vicp.dgiant.test.util;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import net.vicp.dgiant.entry.common.Role;
import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.entry.common.UserRole;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.test.BaseTest;

public class DataInitialization extends BaseTest {
	
	@Autowired
	private UserRoleService service;
	
	@Before
	public void setup() throws SQLException {
		this.createTables(User.class, Role.class, UserRole.class);
		this.clearTables(User.class, Role.class, UserRole.class);
	}
	
	@Test
	public void init() throws SQLException {
		for (int i = 1; i <= 100; i++) {
			String user = "user" + i;
			String email = "gmail.com";
			
			if (i % 2 == 0) {
				email = "hotmail.com";
			}
			
			email = user + "@" +email;
			
			service.createUser(new User(user, null, email));
		}
	}
}
