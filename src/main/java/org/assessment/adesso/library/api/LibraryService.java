package org.assessment.adesso.library.api;

import org.assessment.adesso.common.exceptions.BusinessException;
import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.members.Person;

import java.util.List;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public interface LibraryService {

	List<Book> getAllBooks();

	List<Person> getAllMembers();

	void addNewBook(Person person, Book bookToBeAdded) throws BusinessException;

	void addNewMember(Person person, Person personToBeAdded) throws BusinessException;

	void lendBook(Person person, Book bookToBeLend) throws BusinessException;

	void returnBook(Person person, Book bookToBeReturned) throws BusinessException;

	void removeBookFromLibrary(Person person, Book bookToBeRemoved) throws BusinessException;
}
