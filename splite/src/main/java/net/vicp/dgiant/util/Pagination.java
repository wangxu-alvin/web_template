package net.vicp.dgiant.util;

import java.util.List;

public interface Pagination<T> {

	public List<T> getRows();
	
	public long getTotal();
	
	public String getFooter();
}
