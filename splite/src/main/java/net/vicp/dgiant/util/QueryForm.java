package net.vicp.dgiant.util;

import org.springframework.stereotype.Component;

@Component
public class QueryForm {
	
	private String type;
	
	private String condition;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}
}
