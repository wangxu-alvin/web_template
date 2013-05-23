package net.vicp.dgiant.service.common;

import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;

import net.vicp.dgiant.entry.common.Role;
import net.vicp.dgiant.entry.common.User;
import net.vicp.dgiant.entry.common.UserRole;
import net.vicp.dgiant.util.DataExpiredException;
import net.vicp.dgiant.util.Pagination;
import net.vicp.dgiant.util.RawResultPagination;
import net.vicp.dgiant.util.RowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.SelectArg;
import com.j256.ormlite.support.DatabaseResults;

@Service
public class UserRoleService {

	private Logger logger = (Logger) LoggerFactory
			.getLogger(UserRoleService.class);

	@Resource
	private Dao<User, Integer> userDao;

	@Resource
	private Dao<UserRole, Integer> userRoleDao;

	@Resource
	private Dao<Role, Integer> roleDao;

	public void createUser(User user, int... roleIds) throws SQLException {

		userDao.create(user);
		
		logger.info("create {} ", user.toString());

		if (roleIds != null) {
			for (int roleId : roleIds) {

				logger.info("create user with role id {} ", roleId);

				userRoleDao.createIfNotExists(new UserRole(user, roleDao
						.queryForId(roleId)));
			}
		}
	}
	
	public List<User> queryUsers() throws SQLException {
		
		logger.info("Users queryForAll");
		
		return userDao.queryForAll();
	}

	public void updateUser(User user) throws SQLException, DataExpiredException {

		logger.info("update {} ", user.toString());
		
		int result = userDao.update(user);
		
		if (result == 0)
		{
			throw new DataExpiredException("");
		}
	}

	public void updateUserRoles(int userId, int... roleIds) throws SQLException {

		if (roleIds != null && roleIds.length != 0) {

			User user = userDao.queryForId(userId);

			if (user == null) {

				logger.error("userId[{}] does not exist in DB", userId);

				throw new IllegalArgumentException("userId[" + userId
						+ "] does not exist in DB");
			}

			for (int roleId : roleIds) {
				Role role = roleDao.queryForId(roleId);
				if (role == null) {

					logger.error("roleId[{}] does not exist in DB", roleId);

					throw new IllegalArgumentException("roleId[" + roleId
							+ "] does not exist in DB");
				}
				logger.info("update user with role id {} ", roleId);
				userRoleDao.create(new UserRole(user, role));
			}

		} else {

			logger.info("update user id {} : empty roles", userId);

			deleteUserRoles(userId);
		}
	}

	public User queryUserById(int id) throws SQLException {

		logger.info("queryUserById : {} ", id);
		
		return userDao.queryForId(id);
	}

	/**
	 * This is a common query by user name
	 * @param name query condition
	 * @return all the related users
	 * @throws SQLException
	 */
	public List<User> queryUsersByName(String name) throws SQLException {

		logger.info("queryUsersByName : {} ", name);

		return userDao.query(prepareNameQuery(name));
	}
	
	/**
	 * This is a paginated query by user name
	 * @param name name query condition
	 * @param pageNum requested page number
	 * @param pageCapacity the size of showing in one page
	 * @param url foot links for pages, such as previous page, next page
	 * @return Pagination.getData:a page of users, Pagination.getFooter:foot URL
	 * @throws SQLException
	 */
	public Pagination<User> queryUsersByName(String name, int pageNum,
			int pageCapacity, String url) throws SQLException {
		
		return queryPaginatedUsers(pageNum, pageCapacity, url, prepareNameQuery(name));
	}
	
	private PreparedQuery<User> prepareNameQuery(String name) throws SQLException
	{
		return userDao.queryBuilder().orderBy("id", true).where()
				.like("name", "%" + name + "%").prepare();
	}

	public List<User> queryUsersByEmail(String email) throws SQLException {

		logger.info("queryUsersByEmail : {} ", email);

		return userDao.query(userDao.queryBuilder().orderBy("id", true).where()
				.like("email", "%" + email + "%").prepare());
	}

	/**
	 * Build our query for User objects that match a Role
	 */
	public List<User> queryUsersByRoleId(int roleId) throws SQLException {

		logger.info("queryUsersByRoleId : {} ", roleId);

		QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
		// this time querying for the user-id field
		userRoleQb.selectColumns("user_id");
		userRoleQb.where().eq("role_id", roleId);

		// build our outer query
		QueryBuilder<User, Integer> userQb = userDao.queryBuilder().orderBy(
				"id", true);
		// where the user-id matches the inner query's user-id field
		userQb.where().in("id", userRoleQb);
		
		return userDao.query(userQb.prepare());
	}

	public List<Role> queryRolesByUserId(int userId) throws SQLException {

		logger.info("queryRolesByUserId : {} ", userId);

		QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
		userRoleQb.selectColumns("role_id");
		userRoleQb.where().eq("user_id", userId);
		QueryBuilder<Role, Integer> roleQb = roleDao.queryBuilder().orderBy(
				"id", true);
		roleQb.where().in("id", userRoleQb);

		return roleDao.query(roleQb.prepare());
	}

	public void deleteUserRoles(int userId) throws SQLException {

		logger.info("deleteUserRoles : {} ", userId);

		QueryBuilder<UserRole, Integer> userRoleQb = userRoleDao.queryBuilder();
		userRoleQb.where().eq("user_id", userId);
		userRoleDao.delete(userRoleDao.query(userRoleQb.prepare()));
	}

	public void deleteUser(int userId) throws SQLException {

		logger.info("deleteUser : {} ", userId);

		deleteUserRoles(userId);
		userDao.deleteById(userId);
	}

	public void createRoles(String... names) throws SQLException {
		if (names == null) {
			return;
		}

		for (String name : names) {

			logger.info("create role : {} ", name);

			roleDao.create(new Role(name));
		}
	}
	
	public List<Role> queryRoles() throws SQLException {
		
		logger.info("Roles queryForAll");
		
		return roleDao.queryForAll();
	}
	
	public Role queryRoleById(int roleId) throws SQLException {

			logger.info("queryRoleById id : {} ", roleId);

			return roleDao.queryForId(roleId);
	}

	public void updateRole(int roleId, String newRoleName) throws SQLException {

		logger.info("updateRole : roleId {},  newRoleName {}", roleId,
				newRoleName);

		Role role = roleDao.queryForId(roleId);
		role.setName(newRoleName);
		roleDao.update(role);
	}

	public void deleteRoles(int... roleIds) throws SQLException {
		if (roleIds == null) {
			return;
		}

		// query the user-role mapping
		SelectArg roleIdArg = new SelectArg();
		QueryBuilder<UserRole, Integer> qb = userRoleDao.queryBuilder();
		qb.where().eq("role_id", roleIdArg).prepare();

		for (int roleId : roleIds) {

			logger.info("deleteRole : roleId {}", roleId);

			roleIdArg.setValue(roleId);
			// remove the mappings
			userRoleDao.delete(qb.query());
			// remove the role
			roleDao.deleteById(roleId);
		}
	}
	
	public Pagination<User> queryPaginatedUsers(int pageNum, int pageCapacity,
			String url, PreparedQuery<User> condition) {
		
		RawResultPagination<User> pagination = new RawResultPagination<User>(pageNum,
				pageCapacity, url, userDao, condition);
		pagination.execute(new RowMapper<User>() {
			@Override
			public User mapRow(DatabaseResults rs) throws SQLException{
				User user = new User();
				user.setId(rs.getInt(0));
				user.setName(rs.getString(1));
				return user;
			}
		});
		return pagination;
	}
}
