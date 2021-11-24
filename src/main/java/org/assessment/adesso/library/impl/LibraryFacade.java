package org.assessment.adesso.library.impl;

import org.assessment.adesso.library.api.LibraryService;
import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.members.Person;
import org.assessment.adesso.library.impl.members.type.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
@Service
public class LibraryFacade implements LibraryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(LibraryFacade.class);

	public static List<Book> allBooks;
	public static List<Person> allPersons;

	@Override
	public void addNewBook(Person person, Book bookToBeAdded) throws ForbiddenException {
		if (person.getRole() != Role.LIBRARIAN) {
			String errorMessage = String.format("The calling person with id %d is not allowed to add books to the library",
					person.getId());
			LOGGER.error(errorMessage);
			throw new ForbiddenException(errorMessage, 4003);
		} else {
			allBooks.add(bookToBeAdded);
		}
	}

	@Override
	public List<Book> getAllBooks() {
		return allBooks;
	}

	@Override
	public List<Person> getAllPersons() {
		return allPersons;
	}
}
