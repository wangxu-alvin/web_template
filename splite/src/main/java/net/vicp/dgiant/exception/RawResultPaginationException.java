package net.vicp.dgiant.exception;

public class RawResultPaginationException extends PaginationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public RawResultPaginationException(String errorMessage) {
		super(errorMessage);
	}

	public RawResultPaginationException() {
		super();
	}

}
