package net.vicp.dgiant.pagination.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class CapacitySelect extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private int pageCapacity;
	
	public int getPageCapacity() {
		return pageCapacity;
	}

	public void setPageCapacity(int pageCapacity) {
		this.pageCapacity = pageCapacity;
	}

	@Override
	public int doStartTag() throws JspException {
		
		try {
			// Get the writer object for output.
			JspWriter out = pageContext.getOut();
			String capacity = "<select id=\"capacity\" onchange=\"submitForm(1)\">" + 
					"<option value=\"10\">10</option>" + 
					"<option value=\"20\">20</option>" + 
					"<option value=\"30\">30</option>" + 
					"<option value=\"40\">40</option>" + 
					"<option value=\"50\">50</option>" + 
					"</select>";
			String function = "<script language=\"javascript\">" + 
					"document.getElementById(\"capacity\").value = "  + pageCapacity + 
					";</script>";
			out.println(capacity);
			out.println(function);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}
}
