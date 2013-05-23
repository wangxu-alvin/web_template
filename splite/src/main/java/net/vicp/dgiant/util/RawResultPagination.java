package net.vicp.dgiant.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.support.DatabaseResults;

public class RawResultPagination<T> implements Pagination<T> {

	private Logger logger = (Logger) LoggerFactory
			.getLogger(RawResultPagination.class);

	private int requestedPage = 0;

	private int pageCapacity = 0;

	private String url;

	private Dao<T, Integer> dao;

	private PreparedQuery<T> pq;

	private List<T> results;
	
	private String footer;

	public RawResultPagination(int requestedPage, int pageCapacity, String url,
			Dao<T, Integer> dao, PreparedQuery<T> pq) {

		this.requestedPage = requestedPage;
		this.pageCapacity = pageCapacity;
		this.url = url;

		logger.info("Pagination, [currentPage={}, pageSize={}]", requestedPage,
				pageCapacity);

		this.dao = dao;
		this.pq = pq;

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

	public void execute(RowMapper<T> rowMapper) {
		generateResult(rowMapper);
		generateFooter();
	}

	private void generateResult(RowMapper<T> rowMapper) {
		CloseableIterator<T> iterator = null;
		DatabaseResults drs = null;

		try {
			iterator = (pq == null ? dao.iterator() : dao.iterator(pq));
			drs = iterator.getRawResults();
			drs.moveAbsolute((requestedPage - 1) * pageCapacity);

			int count = 0;

			while (drs.next() && (count < pageCapacity)) {
				results.add(rowMapper.mapRow(drs));
				count++;
			}
		} catch (SQLException e) {

			logger.error(e.getMessage());

		} finally {
			if (drs != null) {
				drs.closeQuietly();
			}
			if (iterator != null) {
				iterator.closeQuietly();
			}
		}
	}

	private void generateFooter() {
		long amount = 0;
		try {
			amount = (pq == null ? dao.countOf() : dao.countOf(pq));
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}

		long pageSize = (amount / pageCapacity)
				+ ((amount % pageCapacity) > 0 ? 1 : 0);

		if (requestedPage > pageSize || requestedPage <= 0) {

			logger.error("requestedPage[{}] is not between 0 and pageSize[{}]",
					requestedPage, pageSize);
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
