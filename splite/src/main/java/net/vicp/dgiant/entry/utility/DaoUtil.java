package net.vicp.dgiant.entry.utility;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;

public class DaoUtil {
	
	private DaoUtil()
	{
		
	}
	
	public static PreparedQuery<?> getPreparedQuery(Dao<?, Integer> dao)
			throws SQLException {
		return dao.queryBuilder().prepare();
	}

}
