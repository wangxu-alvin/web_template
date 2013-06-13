package net.vicp.dgiant.pagination.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.exception.RawResultPaginationException;
import net.vicp.dgiant.pagination.Pagination;
import net.vicp.dgiant.util.RowMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.query.OrderBy;
import com.j256.ormlite.support.DatabaseResults;

public class RawResultPagination<T> implements Pagination<T> {
	
	private Logger logger = (Logger) LoggerFactory
			.getLogger(RawResultPagination.class);

	private int requestedPage = 0;

	private int pageCapacity = 0;

	private List<T> rows;
	
	private long total;
	
	private PaginationQuery<T, Integer> query;

	public RawResultPagination(int requestedPage, int pageCapacity,
			PaginationQuery<T, Integer> query) {

		this.requestedPage = requestedPage;
		this.pageCapacity = pageCapacity;

		logger.debug("Pagination, [currentPage={}, pageSize={}]", requestedPage,
				pageCapacity);
		
		this.query = query;

		rows = new ArrayList<T>(pageCapacity);
	}

	@Override
	public List<T> getRows() {
		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}
	
	@Override
	public int getCurrentPage() {
		return this.requestedPage;
	}

	@Override
	public int getPageCapacity() {
		return this.pageCapacity;
	}
	
	public void execute() throws PaginationException
	{
		generateTotal();
		generateResult(null);
	}
	
	public void execute(RowMapper<T> rowMapper) throws PaginationException
	{
		generateTotal();
		generateResult(rowMapper);
	}
	
	private void generateTotal() throws RawResultPaginationException {
		try {
			query.getBuilder().setCountOf(true);
			total = query.getDao().countOf(query.getBuilder().prepare());
		} catch (SQLException e) {
			
			logger.debug(e.getMessage());
			
			throw new RawResultPaginationException(e.getMessage());
		}
	}

	private void generateResult(RowMapper<T> rowMapper) throws  RawResultPaginationException {
		CloseableIterator<T> iterator = null;
		DatabaseResults drs = null;
		
		long pageSize = (total / pageCapacity)
				+ ((total % pageCapacity) > 0 ? 1 : 0);

		if (requestedPage > pageSize) {

			logger.error("requestedPage[{}] is greater than pageSize[{}]",
					requestedPage, pageSize);
			requestedPage = new Long(pageSize).intValue();
		}

		if (requestedPage <= 0) {

			logger.error("requestedPage[{}] is improper", requestedPage);
			requestedPage = 1;
		}
		
		if (rowMapper == null) {
			rowMapper = new CommonRowMapper<T>(query.getDao().getDataClass());
		}

		try {
			for (OrderBy orderBy : query.getOrderByList()) {
				query.getBuilder().orderBy(orderBy.getColumnName(),
						orderBy.isAscending());
			}
			query.getBuilder().setCountOf(false);
			iterator = query.getDao().iterator(query.getBuilder().prepare());
			drs = iterator.getRawResults();
			if (requestedPage != 1) {
				drs.moveAbsolute((requestedPage - 1) * pageCapacity);
			}
				
			int count = 0;

			while (drs.next() && (count < pageCapacity)) {
				rows.add(rowMapper.mapRow(drs));
				count++;
			}
		} catch (SQLException e) {

			logger.debug(e.getMessage());

			throw new RawResultPaginationException(e.getMessage());

		} finally {
			if (drs != null) {
				drs.closeQuietly();
			}
			if (iterator != null) {
				iterator.closeQuietly();
			}
		}
	}
}
