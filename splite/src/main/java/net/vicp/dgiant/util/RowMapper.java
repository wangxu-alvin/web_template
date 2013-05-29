package net.vicp.dgiant.util;

import java.sql.SQLException;

import com.j256.ormlite.support.DatabaseResults;

public interface RowMapper<T> {

	public T mapRow(DatabaseResults rs) throws SQLException;

}
