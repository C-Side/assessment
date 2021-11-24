package org.assessment.adesso.common.exceptions;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public enum BusinessExceptionType {

	BOOK_NOT_FOUND(4000),
	BOOK_UNAVAILABLE(4001),
	NEW_LIBRARIAN(4002),
	FORBIDDEN(4003),
	TOO_MANY_BOOKS(4004),
	BOOK_NOT_IN_POSSESSION(4005);

	private final int errorCode;

	BusinessExceptionType(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
