package net.vicp.dgiant.exception;

public class PaginationException extends Exception {

	private static final long serialVersionUID = 1L;

	public PaginationException(String errorMessage) {
		super(errorMessage);
	}

	public PaginationException() {
		super();
	}

}
