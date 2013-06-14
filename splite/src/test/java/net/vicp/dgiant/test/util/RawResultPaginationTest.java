package net.vicp.dgiant.test.util;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.exception.DataExpiredException;
import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.pagination.impl.PaginationQuery;
import net.vicp.dgiant.pagination.impl.RawResultPagination;
import net.vicp.dgiant.service.common.UserRoleService;
import net.vicp.dgiant.test.BaseTest;
import net.vicp.dgiant.util.RowMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseResults;

public class RawResultPaginationTest extends BaseTest {

	@Resource
	private Dao<User, Integer> userDao;
	
	@Resource
	private MessageSource messageSource;
	
	@Autowired
	private UserRoleService service;

	private RawResultPagination<User> pagination;

	@Before
	public void setUp() throws SQLException {
		createTables(User.class);
		for (int i = 1; i <= 100; i++) {
			userDao.create(new User("user" + i, "password", "email"));
		}
	}

//	@Test
//	public void getFooter() throws PaginationException {
//
//		pagination = new RawResultPagination<User>(1, 50, "link", userDao);
//		pagination.execute(new RowMapper<User>() {
//			@Override
//			public User mapRow(DatabaseResults rs) throws SQLException {
//				User user = new User();
//				user.setId(rs.getInt(0));
//				user.setName(rs.getString(1));
//				return user;
//			}
//		});
//		Assert.assertEquals("Previous  1  <a href=link?page=2>Next</a>",
//				pagination.getFooter());
//
//	}

	@Test
	public void query() throws SQLException, PaginationException {
		pagination = new RawResultPagination<User>(2, 50, new PaginationQuery<User, Integer>(userDao));
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user51", pagination.getRows().get(0).getName());
		Assert.assertEquals(50, pagination.getRows().size());

		pagination = new RawResultPagination<User>(4, 25, new PaginationQuery<User, Integer>(userDao));
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user76", pagination.getRows().get(0).getName());
		Assert.assertEquals(25, pagination.getRows().size());

		pagination = new RawResultPagination<User>(5, 21, new PaginationQuery<User, Integer>(userDao));
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user85", pagination.getRows().get(0).getName());
		Assert.assertEquals(16, pagination.getRows().size());
	}
	
	@Test
	public void commonRowMapper() throws PaginationException {
		pagination = new RawResultPagination<User>(5, 21, new PaginationQuery<User, Integer>(userDao));
		pagination.execute();

		Assert.assertEquals("user85", pagination.getRows().get(0).getName());
		Assert.assertEquals("password", pagination.getRows().get(0).getPassword());
		Assert.assertEquals("email", pagination.getRows().get(0).getEmail());
		Assert.assertEquals(
				new SimpleDateFormat("yyyyMMddHH").format(new Date()),
				new SimpleDateFormat("yyyyMMddHH").format(pagination.getRows()
						.get(0).getLastModified()));
	}
	
	@Test
	public void commonRowMapperWithSelectColumns() throws PaginationException {
		PaginationQuery<User, Integer> query = new PaginationQuery<User, Integer>(userDao);
		query.selectColumns("email", "name");
		pagination = new RawResultPagination<User>(5, 21, query);
		pagination.execute();

		Assert.assertEquals("user85", pagination.getRows().get(0).getName());
		Assert.assertEquals("email", pagination.getRows().get(0).getEmail());
		// id field will be selected by default
		Assert.assertNotNull("id", pagination.getRows().get(0).getId());
		Assert.assertNull(pagination.getRows().get(0).getLastModified());
		Assert.assertNull(pagination.getRows().get(0).getPassword());
	}
	
	@Test
	public void rawResultUpdateUser() throws PaginationException {
		pagination = new RawResultPagination<User>(5, 21, new PaginationQuery<User, Integer>(userDao));
		pagination.execute();
		
		try {
			User user = pagination.getRows().get(0);
			user.setName("user85_new");
			service.updateUser(user);
			Assert.assertTrue(true);
		} catch (DataExpiredException e) {
			Assert.assertTrue(false);
		} catch (SQLException e) {
			Assert.assertTrue(false);
		}
	}

	@After
	public void tearDown() throws SQLException {
		dropTables(User.class);
	}
}
