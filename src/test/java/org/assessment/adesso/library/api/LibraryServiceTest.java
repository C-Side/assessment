package org.assessment.adesso.library.api;

import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.members.Person;
import org.assessment.adesso.library.impl.members.type.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
@SpringBootTest
public class LibraryServiceTest {

	private Person librarian;
	private Person member;

	@Autowired
	private LibraryService libraryService;

	@BeforeEach
	public void setupTestData() {
		this.librarian = new Person(1,
				"Anna",
				"TestName",
				null,
				Role.LIBRARIAN);
		this.member = new Person(2,
				"Max",
				"LastName",
				null,
				Role.MEMBER);
	}

	@Test
	public void addNewBooksTest() {
		Book testBook = new Book();
		assertThatThrownBy(() -> libraryService.addNewBook(member, testBook))
				.isInstanceOf(ForbiddenException.class)
				.hasMessage("The calling person with id 2 is not allowed to add books to the library")
				.extracting("errorCode").isEqualTo(4003);
	}
}
