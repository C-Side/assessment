package org.assessment.adesso.common.exceptions;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public abstract class BusinessException extends Exception {

	private final int errorCode;

	public BusinessException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return errorCode;
	}
}
