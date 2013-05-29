package net.vicp.dgiant.test.util;

import java.sql.SQLException;

import javax.annotation.Resource;

import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.test.BaseTest;
import net.vicp.dgiant.util.RawResultPagination;
import net.vicp.dgiant.util.RowMapper;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseResults;

public class RawResultPaginationTest extends BaseTest {

	@Resource
	private Dao<User, Integer> userDao;

	private RawResultPagination<User> pagination;

	@Before
	public void setUp() throws SQLException {
		createTables(User.class);
		for (int i = 1; i <= 100; i++) {
			userDao.create(new User("user" + i, "", ""));
		}
	}

	@Test
	public void getFooter() throws PaginationException {

		pagination = new RawResultPagination<User>(1, 50, "link", userDao);
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});
		Assert.assertEquals("Previous  1  <a href=link?page=2>Next</a>",
				pagination.getFooter());

	}

	@Test
	public void query() throws SQLException, PaginationException {
		pagination = new RawResultPagination<User>(2, 50, "link", userDao);
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user51", pagination.getData().get(0).getName());
		Assert.assertEquals(50, pagination.getData().size());

		pagination = new RawResultPagination<User>(4, 25, "link", userDao);
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user76", pagination.getData().get(0).getName());
		Assert.assertEquals(25, pagination.getData().size());

		pagination = new RawResultPagination<User>(5, 21, "link", userDao);
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException {
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});

		Assert.assertEquals("user85", pagination.getData().get(0).getName());
		Assert.assertEquals(16, pagination.getData().size());
	}

	@After
	public void tearDown() throws SQLException {
		dropTables(User.class);
	}
}
