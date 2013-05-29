package net.vicp.dgiant.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.exception.RawResultPaginationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.DatabaseResults;

public class RawResultPagination<T> implements Pagination<T> {

	private Logger logger = (Logger) LoggerFactory
			.getLogger(RawResultPagination.class);

	private int requestedPage = 0;

	private int pageCapacity = 0;

	private String url;

	private Dao<T, Integer> dao;

	private QueryBuilder<T, Integer> builder;

	private List<T> results;

	private String footer;

	public RawResultPagination(int requestedPage, int pageCapacity, String url,
			Dao<T, Integer> dao, QueryBuilder<T, Integer> builder) {

		this.requestedPage = requestedPage;
		this.pageCapacity = pageCapacity;
		this.url = url;

		logger.info("Pagination, [currentPage={}, pageSize={}]", requestedPage,
				pageCapacity);

		this.dao = dao;

		this.builder = builder;
		results = new ArrayList<T>(pageCapacity);
	}

	public RawResultPagination(int requestedPage, int pageCapacity, String url,
			Dao<T, Integer> dao) {

		this(requestedPage, pageCapacity, url, dao, null);
	}

	@Override
	public String getFooter() {

		return footer;
	}

	@Override
	public List<T> getData() {

		return results;
	}

	@Override
	public void execute() throws PaginationException
	{
		generateFooter();
		generateResult(null);
	}
	
	public void execute(RowMapper<T> rowMapper) throws PaginationException
	{
		generateFooter();
		generateResult(rowMapper);
	}

	private void generateResult(RowMapper<T> rowMapper) throws  RawResultPaginationException {
		CloseableIterator<T> iterator = null;
		DatabaseResults drs = null;
		
		if (rowMapper == null) {
			rowMapper = new RowMapperImpl<T>(dao.getDataClass());
		}

		try {
			if (builder != null) {
				builder.setCountOf(false);
				iterator = dao.iterator(builder.prepare());
			} else {
				iterator = dao.iterator();
			}
			drs = iterator.getRawResults();
			if (requestedPage != 1) {
				drs.moveAbsolute((requestedPage - 1) * pageCapacity);
			}
				
			int count = 0;

			while (drs.next() && (count < pageCapacity)) {
				results.add(rowMapper.mapRow(drs));
				count++;
			}
		} catch (SQLException e) {

			logger.error(e.getMessage());

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
	
	private void generateFooter() throws RawResultPaginationException {
		long amount = 0;
		try {
			if (builder != null) {
				builder.setCountOf(true);
				amount = dao.countOf(builder.prepare());
			} else {
				amount = dao.countOf();
			}
		} catch (SQLException e) {
			
			logger.error(e.getMessage());
			
			throw new RawResultPaginationException(e.getMessage());
		}

		long pageSize = (amount / pageCapacity)
				+ ((amount % pageCapacity) > 0 ? 1 : 0);

		if (requestedPage > pageSize) {

			logger.error("requestedPage[{}] is greater than pageSize[{}]",
					requestedPage, pageSize);
			requestedPage = new Long(pageSize).intValue();
		}

		if (requestedPage <= 0) {

			logger.error("requestedPage[{}] is improper", requestedPage);
			requestedPage = 1;
		}

		StringBuffer sbFooter = new StringBuffer();
		sbFooter.append(requestedPage == 1 ? "Previous" : url(
				requestedPage - 1, "Previous"));
		sbFooter.append("  " + requestedPage + "  ");
		sbFooter.append(requestedPage == pageSize ? "Next" : url(
				requestedPage + 1, "Next"));

		footer = sbFooter.toString();
	}

	// TODO need to be refined
	private String url(int page, String... mark) {
		return "<a href=" + url + "?page=" + page + ">"
				+ (mark.length == 0 ? " page " + page : mark[0]) + "</a>";
	}
}
