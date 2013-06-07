package net.vicp.dgiant.util;

import java.text.DateFormat;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;

/**
 * Also support long value for Date comparing with CustomDateEditor
 */
public class CompatibleDateEditor extends CustomDateEditor {

	public CompatibleDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		super(dateFormat, allowEmpty);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		try {
			setValue(new Date(Long.parseLong(text)));
		} catch (NumberFormatException e) {
			super.setAsText(text);
		}
	}
}
