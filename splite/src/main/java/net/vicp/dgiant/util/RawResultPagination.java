package net.vicp.dgiant.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import net.vicp.dgiant.exception.PaginationException;
import net.vicp.dgiant.exception.RawResultPaginationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

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

	private List<T> rows;
	
	private long total;

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
		rows = new ArrayList<T>(pageCapacity);
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
	public List<T> getRows() {

		return rows;
	}

	@Override
	public long getTotal() {
		return total;
	}
	
	public void execute() throws PaginationException
	{
		generateTotal();
		generateFooter();
		generateResult(null);
	}
	
	public void execute(RowMapper<T> rowMapper) throws PaginationException
	{
		generateTotal();
		generateFooter();
		generateResult(rowMapper);
	}

	private void generateResult(RowMapper<T> rowMapper) throws  RawResultPaginationException {
		CloseableIterator<T> iterator = null;
		DatabaseResults drs = null;
		
		if (rowMapper == null) {
			rowMapper = new CommonRowMapper<T>(dao.getDataClass());
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
				rows.add(rowMapper.mapRow(drs));
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
	
	private void generateTotal() throws RawResultPaginationException {
		try {
			if (builder != null) {
				builder.setCountOf(true);
				total = dao.countOf(builder.prepare());
			} else {
				total = dao.countOf();
			}
		} catch (SQLException e) {
			
			logger.error(e.getMessage());
			
			throw new RawResultPaginationException(e.getMessage());
		}
	}
	
	private void generateFooter() throws RawResultPaginationException {
		
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
		
		Locale locale = LocaleContextHolder.getLocale();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setDefaultEncoding("UTF-8");
		
		String previous = messageSource.getMessage("pagination.previous", null, locale);
		String next = messageSource.getMessage("pagination.next", null, locale);
		String current = messageSource.getMessage("pagination.current", null, locale);
		String total = messageSource.getMessage("pagination.total", new Object[]{pageSize}, locale);

		StringBuffer sbFooter = new StringBuffer();
		sbFooter.append("  " + current + " " + requestedPage + " ");
		sbFooter.append(requestedPage == 1 ? previous : url(
				requestedPage - 1, previous));
		sbFooter.append(requestedPage >= pageSize ? next : url(
				requestedPage + 1, next));
		sbFooter.append(" " + total);

		footer = sbFooter.toString();
	}

	private String url(int page, String... mark) {
		return "<a href=" + url + "?page=" + page + ">"
				+ (mark.length == 0 ? " page " + page : mark[0]) + "</a>";
	}
}
