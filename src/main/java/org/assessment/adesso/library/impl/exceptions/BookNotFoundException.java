package org.assessment.adesso.library.impl.exceptions;

import org.assessment.adesso.common.exceptions.BusinessException;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class BookNotFoundException extends BusinessException {

	public BookNotFoundException(String message, int errorCode) {
		super(message, errorCode);
	}
}
