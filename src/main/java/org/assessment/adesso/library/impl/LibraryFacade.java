package org.assessment.adesso.library.impl;

import org.assessment.adesso.common.exceptions.BusinessException;
import org.assessment.adesso.common.exceptions.BusinessExceptionType;
import org.assessment.adesso.library.api.LibraryService;
import org.assessment.adesso.library.impl.books.Book;
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
	public List<Book> getAllBooks() {
		return allBooks;
	}

	@Override
	public List<Person> getAllMembers() {
		return allPersons.stream()
				.filter(person -> person.getRole() == Role.MEMBER)
				.collect(Collectors.toList());
	}

	@Override
	public void addNewBook(Person person, Book bookToBeAdded) throws BusinessException {
		throwExceptionIfMember(person, "add new books to the library");
		allBooks.add(bookToBeAdded);
	}

	@Override
	public void addNewMember(Person person, Person personToBeAdded) throws BusinessException {
		throwExceptionIfMember(person, "add new members");

		if (personToBeAdded.getRole() == Role.LIBRARIAN) {
			String errorMessage = "The new person does not have the role MEMBER";
			logErrorAndThrowException(errorMessage, BusinessExceptionType.NEW_LIBRARIAN);
		} else {
			allPersons.add(personToBeAdded);
		}
	}

	@Override
	public void lendBook(Person person, Book bookToBeLend) throws BusinessException {
		throwExceptionIfLibrarian(person, "lend books");
		checkLentBooks(person);

		try {
			Book bookFromStock = allBooks.get(allBooks.indexOf(bookToBeLend));
			if (bookFromStock.isAvailable()) {
				person.getBooks().add(bookToBeLend);
				bookFromStock.setLentToPerson(person);
				bookFromStock.setAvailable(false);
			} else {
				String errorMessage = String.format("The book with id %d is not available.",
						bookFromStock.getId());
				logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_UNAVAILABLE);
			}
		} catch (IndexOutOfBoundsException exception) {
			String errorMessage = String.format("The book with title %s was not found in the library.",
					bookToBeLend.getTitle());
			logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_NOT_FOUND);
		}
	}

	@Override
	public void returnBook(Person person, Book bookToBeReturned) throws BusinessException {
		throwExceptionIfLibrarian(person, "return books");
		try {
			Book bookFromStock = allBooks.get(allBooks.indexOf(bookToBeReturned));
			bookFromStock.setAvailable(true);
			bookFromStock.setLentToPerson(null);
			boolean bookWasInPossession = person.getBooks().remove(bookToBeReturned);
			if (!bookWasInPossession) {
				String errorMessage = String.format("The book with title %s was never in the possession of the person.",
						bookToBeReturned.getTitle());
				logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_NOT_IN_POSSESSION);
			}
		} catch (IndexOutOfBoundsException exception) {
			String errorMessage = String.format("The book with title %s was not found in the library.",
					bookToBeReturned.getTitle());
			logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_NOT_FOUND);
		}
	}

	@Override
	public void removeBookFromLibrary(Person person, Book bookToBeRemoved) throws BusinessException {
		throwExceptionIfMember(person, "remove books from the library");
		try {
			Book bookFromStock = allBooks.get(allBooks.indexOf(bookToBeRemoved));
			if (bookFromStock.isAvailable()) {
				allBooks.remove(bookToBeRemoved);
			} else {
				String errorMessage = String.format("The book with id %d is not available.",
						bookFromStock.getId());
				logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_UNAVAILABLE);
			}
		} catch (IndexOutOfBoundsException exception) {
			String errorMessage = String.format("The book with title %s was not found in the library.",
					bookToBeRemoved.getTitle());
			logErrorAndThrowException(errorMessage, BusinessExceptionType.BOOK_NOT_FOUND);
		}
	}

	@Override
	public List<Book> getAllCopiesOfBook(Book typeOfBook) {
		return allBooks.stream()
				.filter(book -> book.equals(typeOfBook))
				.collect(Collectors.toList());
	}

	private void throwExceptionIfMember(Person person, String message) throws BusinessException {
		if (person.getRole() == Role.MEMBER) {
			String errorMessage = String.format("The calling person with id %d is not allowed to %s.",
					person.getId(),
					message);
			logErrorAndThrowException(errorMessage, BusinessExceptionType.FORBIDDEN);
		}
	}

	private void throwExceptionIfLibrarian(Person person, String message) throws BusinessException {
		if (person.getRole() == Role.LIBRARIAN) {
			String errorMessage = String.format("The calling person with id %d is not allowed to %s.",
					person.getId(),
					message);
			logErrorAndThrowException(errorMessage, BusinessExceptionType.FORBIDDEN);
		}
	}

	private void checkLentBooks(Person person) throws BusinessException {
		if (person.getBooks().size() == 7) {
			String errorMessage = String.format("The person with id %d already lent seven books.",
					person.getId());
			logErrorAndThrowException(errorMessage, BusinessExceptionType.TOO_MANY_BOOKS);
		}
	}

	private void logErrorAndThrowException(String errorMessage, BusinessExceptionType exceptionType) throws BusinessException {
		LOGGER.error(errorMessage);
		throw new BusinessException(errorMessage, exceptionType);
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
