package net.vicp.dgiant.pagination.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class FormHiddenElements extends TagSupport {

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {

		try {
			// Get the writer object for output.
			JspWriter out = pageContext.getOut();
			out.println("<input type=\"hidden\" name=\"page\">\n<input type=\"hidden\" name=\"capacity\">");

		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

}
