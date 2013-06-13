package net.vicp.dgiant.pagination.impl;

import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.stmt.query.OrderBy;

/**
 * This is for separating order by from query builder.
 * In the pagination case, if we count for the rows and the builder with a order by,
 * Some databases ,e.g. PostgreSQL H2, will raise a weird exception like:
 * 
 *  <I>select count(*) from user order by id;
 *  Column "ID" must be in the GROUP BY list;</I>
 *  
 * @author localmanager
 *
 * @param <T>
 * @param <ID>
 */
public class PaginationQuery<T, ID> {
	
	private Dao<T, ID> dao;
	private QueryBuilder<T, ID> builder;
	private List<OrderBy> orderByList;
	
	public List<OrderBy> getOrderByList() {
		return orderByList;
	}

	public PaginationQuery(Dao<T, ID> dao) {
		this.dao = dao;
		this.builder = dao.queryBuilder();
		orderByList = new ArrayList<OrderBy>();
	}

	Dao<T, ID> getDao() {
		return dao;
	}

	QueryBuilder<T, ID> getBuilder() {
		return builder;
	}
	
	public PaginationQuery<T, ID> orderBy(String columnName, boolean ascending) {
		orderByList.add(new OrderBy(columnName, ascending));
		return this;
	}
	
	public Where<T, ID> where() {
		return builder.where();
	}
	
	public QueryBuilder<T, ID> selectColumns(String...columns) {
		return builder.selectColumns(columns);
	}
}
