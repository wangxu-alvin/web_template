package net.vicp.dgiant.util;

import java.util.List;

public interface Pagination<T> {
	
	public String getFooter();
	
	public List<T> query(RowMapper<T> rowMapper);
	
}
