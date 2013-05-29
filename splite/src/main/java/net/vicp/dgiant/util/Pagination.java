package net.vicp.dgiant.util;

import java.util.List;

import net.vicp.dgiant.exception.PaginationException;

public interface Pagination<T> {

	public String getFooter();

	public List<T> getData();
	
	public void execute() throws PaginationException;

}
