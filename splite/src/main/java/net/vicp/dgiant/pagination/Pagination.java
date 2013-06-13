package net.vicp.dgiant.pagination;

import java.util.List;

public interface Pagination<T> {
	
	public int getCurrentPage();
	
	public int getPageCapacity();

	public List<T> getRows();
	
	public long getTotal();
	
}
