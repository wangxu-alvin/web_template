package net.vicp.dgiant.test;

import java.sql.SQLException;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:test-spring-configuration.xml")
public class BaseTest extends AbstractJUnit4SpringContextTests {
	@Autowired
	private JdbcConnectionSource connectionSource;

	protected void createTables(Class<?>... classes) throws SQLException {
		for (Class<?> clazz : classes) {
			TableUtils.createTableIfNotExists(connectionSource, clazz);
		}
	}

	protected void dropTables(Class<?>... classes) throws SQLException {
		for (Class<?> clazz : classes) {
			TableUtils.dropTable(connectionSource, clazz, false);
		}
	}

	protected JdbcConnectionSource getConnection() {
		return connectionSource;
	}
}
