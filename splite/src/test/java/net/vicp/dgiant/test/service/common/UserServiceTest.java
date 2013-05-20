package net.vicp.dgiant.test.service.common;

import java.sql.SQLException;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.service.common.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest extends AbstractJUnit4SpringContextTests {

	@Autowired
	private JdbcConnectionSource connectionSource;
	
	@Autowired
	private UserService service;
	
	@Before
	public void init() throws SQLException
	{
		TableUtils.createTableIfNotExists(connectionSource, User.class);
	}
	
	@Test
	public void testCreateUser() throws SQLException {
		Assert.notNull(service, "service is null");
		
		User user1 = new User();
		user1.setEmail("sacat.wx@gmail.com");
		user1.setName("wangxu");
		
		Assert.isTrue(service.createUser(user1) == 1, "no rows are updated by the case");
		
		User user2 = service.queryUserById(1);
		Assert.isTrue(user2.getName().equals("wangxu"));
	}
	
	@After
	public void tear() throws SQLException
	{
		TableUtils.dropTable(connectionSource, User.class, false);
	}
}
