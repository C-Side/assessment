package org.assessment.adesso.common.exceptions;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class BusinessException extends Exception {

	private final BusinessExceptionType exceptionType;

	public BusinessException(String message, BusinessExceptionType exceptionType) {
		super(message);
		this.exceptionType = exceptionType;
	}

	public BusinessExceptionType getExceptionType() {
		return exceptionType;
	}
}
