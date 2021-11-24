package org.assessment.adesso.library.api;

import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.exceptions.BookNotFoundException;
import org.assessment.adesso.library.impl.exceptions.BookUnavailableException;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.exceptions.NewLibrarianException;
import org.assessment.adesso.library.impl.members.Person;

import java.util.List;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public interface LibraryService {

	void addNewBook(Person person, Book bookToBeAdded) throws ForbiddenException;

	void addNewMember(Person person, Person personToBeAdded) throws ForbiddenException, NewLibrarianException;

	void lendBook(Person person, Book bookToBeLend) throws ForbiddenException, BookUnavailableException, BookNotFoundException;

	List<Book> getAllBooks();

	List<Person> getAllMembers();
}
