package org.assessment.adesso.library.impl;

import org.assessment.adesso.library.api.LibraryService;
import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.exceptions.BookNotFoundException;
import org.assessment.adesso.library.impl.exceptions.BookUnavailableException;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.exceptions.NewLibrarianException;
import org.assessment.adesso.library.impl.members.Person;
import org.assessment.adesso.library.impl.members.type.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
@Service
public class LibraryFacade implements LibraryService {

	private final static Logger LOGGER = LoggerFactory.getLogger(LibraryFacade.class);

	public static List<Book> allBooks = new ArrayList<>();
	public static List<Person> allPersons = new ArrayList<>();

	static {
		initialSetup();
	}

	@Override
	public void addNewBook(Person person, Book bookToBeAdded) throws ForbiddenException {
		if (person.getRole() != Role.LIBRARIAN) {
			String errorMessage = String.format("The calling person with id %d is not allowed to add new books to the library",
					person.getId());
			LOGGER.error(errorMessage);
			throw new ForbiddenException(errorMessage, 4003);
		} else {
			allBooks.add(bookToBeAdded);
		}
	}

	@Override
	public void addNewMember(Person person, Person personToBeAdded) throws ForbiddenException, NewLibrarianException {
		if (person.getRole() != Role.LIBRARIAN) {
			String errorMessage = String.format("The calling person with id %d is not allowed to add new persons to the library",
					person.getId());
			LOGGER.error(errorMessage);
			throw new ForbiddenException(errorMessage, 4003);
		} else if (personToBeAdded.getRole() == Role.LIBRARIAN) {
			String errorMessage = "The new person does not have the role MEMBER";
			LOGGER.error(errorMessage);
			throw new NewLibrarianException(errorMessage, 4002);
		} else {
			allPersons.add(personToBeAdded);
		}
	}

	@Override
	public void lendBook(Person person, Book bookToBeLend) throws ForbiddenException,
			BookUnavailableException,
			BookNotFoundException {
		if (person.getRole() == Role.LIBRARIAN) {
			String errorMessage = String.format("The calling person with id %d is not allowed lend books.",
					person.getId());
			LOGGER.error(errorMessage);
			throw new ForbiddenException(errorMessage, 4003);
		} else {
			try {
				Book bookFromStock = allBooks.get(allBooks.indexOf(bookToBeLend));
				if (bookFromStock.isAvailable()) {
					person.getBooks().add(bookToBeLend);
					bookFromStock.setAvailable(false);
				} else {
					String errorMessage = String.format("The book with id %d is not available.",
							bookFromStock.getId());
					LOGGER.error(errorMessage);
					throw new BookUnavailableException(errorMessage, 4001);
				}
			} catch (IndexOutOfBoundsException exception) {
				String errorMessage = String.format("The book with title %s was not found in the library.",
						bookToBeLend.getTitle());
				LOGGER.error(errorMessage);
				throw new BookNotFoundException(errorMessage, 4000);
			}
		}
	}

	@Override
	public List<Book> getAllBooks() {
		return allBooks;
	}

	@Override
	public List<Person> getAllMembers() {
		return allPersons.stream()
				.filter(person -> person.getRole() == Role.MEMBER)
				.collect(Collectors.toList());
	}

	private static void initialSetup() {
		Person librarian = new Person(1,
				"Anna",
				"TestName",
				Role.LIBRARIAN);
		allPersons.add(librarian);
		Person member = new Person(2,
				"Max",
				"LastName",
				Role.MEMBER);
		allPersons.add(member);
	}
}
