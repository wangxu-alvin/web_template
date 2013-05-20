package net.vicp.dgiant.test.service.common;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;

import net.vicp.dgiant.entry.common.Role;
import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.entry.common.UserRole;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.test.BaseTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.Assert;

import com.j256.ormlite.support.DatabaseResults;

public class UserRoleServiceTest extends BaseTest {

	@Autowired
	private UserRoleService service;
	
	@Before
	public void init() throws SQLException
	{
		Assert.assertNotNull("service is not initialized", service);
		createTables(User.class, Role.class, UserRole.class);
		
		service.createUser(new User("user1", "pwd1", "user1@gmail.com"));
		service.createUser(new User("user2", "pwd2", "user2@gmail.com"));
		service.createUser(new User("user3", "pwd3", "user3@gmail.com"));
		service.createUser(new User("user4", "pwd4", "user4@gmail.com"));
		service.createUser(new User("user5", "pwd5", "user5@hotmail.com"));
		service.createUser(new User("user6", "pwd6", "user6@hotmail.com"));
		service.createUser(new User("user7", "pwd7", "user7@hotmail.com"));
		
		service.createRoles("role1", "role2", "role3", "role4", "role5",
				"role6", "role7");
	}
	
	@Test
	public void updateUser() throws SQLException {
		
		User user = service.queryUserById(1);
		user.setPassword("pwd1_modified");
		service.updateUser(user);
		service.updateUserRoles(user.getId(), 1);
		
		Assert.assertEquals("role1",
				service.queryRolesByUserId(1).get(0).getName());
		
		Assert.assertEquals("pwd1_modified",
				service.queryUserById(1).getPassword());
	}
	
	@Test
	public void queryUsersByName() throws SQLException {
		List<User> users = service.queryUsersByName("user5");
		Assert.assertEquals(1, users.size());
	}
	
	@Test
	public void queryUsersByEmail() throws SQLException {
		List<User> users = service.queryUsersByEmail("hotmail");
		Assert.assertEquals(3, users.size());
	}
	
	@Test
	public void queryUsersByRoleId() throws SQLException {
		Assert.assertEquals(0, service.queryRolesByUserId(2).size());
		service.updateUserRoles(2, 3);
		Assert.assertEquals(1, service.queryUsersByRoleId(3).size());
	}
	
	@Test
	public void deleteUserRoles() throws SQLException {
		service.updateUserRoles(3, 1, 2);
		Assert.assertEquals(2, service.queryRolesByUserId(3).size());
		service.deleteUserRoles(3);
		Assert.assertEquals(0, service.queryRolesByUserId(3).size());
	}
	
	@Test
	public void deleteUser() throws SQLException {
		service.deleteUser(4);
		Assert.assertNull(service.queryUserById(4));
	}
	
	@Test
	public void updateRole() throws SQLException {
		service.updateRole(7, "role7_modified");
		Assert.assertEquals("role7_modified", service.queryRoleById(7)
				.getName());
	}
	
	@Test
	@Ignore
	public void deleteRoles() throws SQLException {
		service.updateUserRoles(6, 5, 6, 7);
		Assert.assertEquals(3, service.queryRolesByUserId(6).size());
		
		service.deleteRoles(6);
		Assert.assertNull(service.queryRoleById(6));
		Assert.assertEquals(2, service.queryRolesByUserId(6).size());
	}
	
	@Test
	public void queryPaginatedUsers() throws SQLException {
		Field[] fields = User.class.getDeclaredFields();
		for (int i=0; i<fields.length; i++)
		{
			System.out.println(fields[i].getType());
		}
	}
	
	@After
	public void tear() throws SQLException
	{
		dropTables(UserRole.class, User.class, Role.class);
	}
}
