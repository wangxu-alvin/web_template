package net.vicp.dgiant.service.common;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import net.vicp.dgiant.entry.common.Role;
import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.entry.common.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.stmt.QueryBuilder;

@Service
public class UserService {

	@Autowired
	private JdbcConnectionSource connectionSource;
	
	@Resource(name="userDao")
	private Dao<User, Integer> userDao;

	@Resource(name="userRoleDao")
	private Dao<UserRole, Integer> userRoleDao;

	public int createUser(User user, Role...roles) throws SQLException {
		
		int updateRowCount = userDao.create(user);
		
		if (roles != null)
		{
			for (Role role : roles)
			{
				userRoleDao.createIfNotExists(new UserRole(user, role));
			}
		}
		
		return updateRowCount;
	}
	
	public User queryUserById(int id) throws SQLException {
		return userDao.queryForId(id);
	}
	
	public List<User> queryUsersByName(String name) throws SQLException {
		return queryUsersByFieldLike("name", name);
	}
	
	public List<User> queryUsersByEmail(String email) throws SQLException {
		return queryUsersByFieldLike("email", email);
	}
	
	/**
	 * Build our query for User objects that match a Role
	 */
	public List<User> queryUsersByRoleId(int roleId) throws SQLException {
		
		QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
		// this time querying for the user-id field
		userRoleQb.selectColumns("user_id");
		userRoleQb.where().eq("role_id", roleId);

		// build our outer query
		QueryBuilder<User, Integer> userQb = userDao.queryBuilder();
		// where the user-id matches the inner query's user-id field
		userQb.where().in("id", userRoleQb);
		
		return userDao.query(userQb.prepare());
	}
	
	private List<User> queryUsersByFieldLike(String column, String value)
			throws SQLException {
		return userDao.query(userDao.queryBuilder().where().like(column, value)
				.prepare());
	}
	
//	public int deleteUser(User user) throws SQLException {
//		
//		QueryBuilder<UserRole, Integer> qb = userRoleDao.queryBuilder();
//		qb.where().eq("user_id", user.getId());
//		results = userRoleDao.queryRaw(qb.prepareStatementString());
//		
//	}
	
}
