package net.vicp.dgiant.pagination.tag;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class Footer extends TagSupport {
	
	private static final long serialVersionUID = 1L;

	private long total;
	
	private int pageCapacity;
	
	private int requestedPage;
	
	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getPageCapacity() {
		return pageCapacity;
	}

	public void setPageCapacity(int pageCapacity) {
		this.pageCapacity = pageCapacity;
	}

	public int getRequestedPage() {
		return requestedPage;
	}

	public void setRequestedPage(int requestedPage) {
		this.requestedPage = requestedPage;
	}

	@Override
	public int doStartTag() throws JspException {
		Locale locale = LocaleContextHolder.getLocale();
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:message");
		messageSource.setFallbackToSystemLocale(true);
		messageSource.setDefaultEncoding("UTF-8");
		
		long pageSize = (total / pageCapacity)
				+ ((total % pageCapacity) > 0 ? 1 : 0);

		String previous = messageSource.getMessage("pagination.previous", null,
				locale);
		String next = messageSource.getMessage("pagination.next", null, locale);
//		String current = messageSource.getMessage("pagination.current", null,
//				locale);
		String totalLabel = messageSource.getMessage("pagination.total",
				new Object[] { pageSize }, locale);
		String first = messageSource.getMessage("pagination.first", null,
				locale);
		String last = messageSource.getMessage("pagination.last", null,
				locale);

		StringBuffer sbFooter = new StringBuffer();
		
		if (requestedPage <= 1) {
			sbFooter.append(first);
			sbFooter.append("&nbsp");
			sbFooter.append(previous);
		} else {
			sbFooter.append(url(1, first));
			sbFooter.append("&nbsp");
			sbFooter.append(url(requestedPage - 1, previous));
		}
		sbFooter.append("&nbsp");
		sbFooter.append(requestedPage);
		sbFooter.append("&nbsp");
		if (requestedPage >= pageSize) {
			sbFooter.append(next);
			sbFooter.append("&nbsp");
			sbFooter.append(last);
			sbFooter.append("&nbsp");
		} else {
			sbFooter.append(url(requestedPage + 1, next));
			sbFooter.append("&nbsp");
			sbFooter.append(url(new Long(pageSize).intValue(), last));
			sbFooter.append("&nbsp");
		}
		
		sbFooter.append("&nbsp");
		sbFooter.append(totalLabel);
		
		JspWriter out = pageContext.getOut();
		try {
			out.println(sbFooter.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return SKIP_BODY;
	}

	private String url(int page, String des) {
		return "<a href=\"javascript:submitForm("+page+")\">" + des + "</a>";
	}
}
