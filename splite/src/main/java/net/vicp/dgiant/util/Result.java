package net.vicp.dgiant.util;

public class Result {
	
	private boolean success;
	
	private String message;
	
	public Result (boolean success) {
		this.success = success;
	}
	
	public Result (boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public Result success(boolean success) {
		this.success = success;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result message(String message) {
		this.message = message;
		return this;
	}

}
