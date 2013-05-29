package net.vicp.dgiant.util;

import java.sql.SQLException;

import net.vicp.dgiant.exception.RawResultPaginationException;

import com.j256.ormlite.support.DatabaseResults;

public interface RowMapper<T> {

	public T mapRow(DatabaseResults rs) throws RawResultPaginationException, SQLException;

}
