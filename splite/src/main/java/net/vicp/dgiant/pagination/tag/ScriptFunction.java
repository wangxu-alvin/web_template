package net.vicp.dgiant.pagination.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

public class ScriptFunction extends TagSupport {

	private static final long serialVersionUID = 1L;
	
	private String formName;

	@Override
	public int doStartTag() throws JspException {

		try {
			// Get the writer object for output.
			JspWriter out = pageContext.getOut();
			String function = "<script language=\"javascript\">" +
					   "function submitForm(page) {" + 
					   formName + ".page.value = page;\n" +
					   formName + ".capacity.value = document.getElementById(\"capacity\").value;\n" +
					   formName + ".submit();\n" +
					   "}" + 
					   "</script>";
			out.println(function);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return SKIP_BODY;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

}
