package org.assessment.adesso.library.impl.exceptions;

import org.assessment.adesso.common.exceptions.BusinessException;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class BookUnavailableException extends BusinessException {

	public BookUnavailableException(String message, int errorCode) {
		super(message, errorCode);
	}
}
