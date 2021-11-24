package org.assessment.adesso.library.api;

import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.members.Person;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public interface LibraryService {

	void addNewBook(Person person, Book bookToBeAdded) throws ForbiddenException;

	List<Book> getAllBooks();

	List<Person> getAllPersons();
}
