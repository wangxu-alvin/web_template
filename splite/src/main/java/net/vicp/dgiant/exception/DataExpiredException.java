package net.vicp.dgiant.exception;

public class DataExpiredException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataExpiredException(String errorMessage) {
		super(errorMessage);
	}

	public DataExpiredException() {
		super();
	}

}
