package org.assessment.adesso.library.api;

import org.assessment.adesso.library.impl.LibraryFacade;
import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.books.type.Genre;
import org.assessment.adesso.library.impl.exceptions.BookNotFoundException;
import org.assessment.adesso.library.impl.exceptions.BookUnavailableException;
import org.assessment.adesso.library.impl.exceptions.ForbiddenException;
import org.assessment.adesso.library.impl.exceptions.NewLibrarianException;
import org.assessment.adesso.library.impl.members.Person;
import org.assessment.adesso.library.impl.members.type.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.filter;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

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
		this.librarian = LibraryFacade.allPersons.get(0);
		this.member = LibraryFacade.allPersons.get(1);

		Book book1 = new Book(1,
				"TestBook",
				"TestAuthor",
				Genre.FANTASY,
				true);
		LibraryFacade.allBooks.add(book1);
		Book book2 = new Book(2,
				"AnotherTestBook",
				"AnotherTestAuthor",
				Genre.SCI_FI,
				true);
		LibraryFacade.allBooks.add(book2);
		Book book3 = new Book(3,
				"TheThirdTestBook",
				"TheThirdTestAuthor",
				Genre.NON_FICTION,
				false);
		LibraryFacade.allBooks.add(book3);
	}

	@Test
	public void getAllMembers_ShouldReturnMembers() {
		List<Person> allMembers = libraryService.getAllMembers();
		assertThat(allMembers).hasSize(1);
		assertThat(allMembers).first().isEqualTo(this.member);
	}

	@Test
	public void addNewBooksTest_ShouldThrowExceptions() {
		Book testBook = new Book();
		assertThatThrownBy(() -> libraryService.addNewBook(member, testBook))
				.isInstanceOf(ForbiddenException.class)
				.hasMessage("The calling person with id 2 is not allowed to add new books to the library")
				.extracting("errorCode").isEqualTo(4003);
	}

	@Test
	public void addNewMembersTest_ShouldThrowExceptions() {
		assertThatThrownBy(() -> libraryService.addNewMember(member, member))
				.isInstanceOf(ForbiddenException.class)
				.hasMessage("The calling person with id 2 is not allowed to add new persons to the library")
				.extracting("errorCode").isEqualTo(4003);

		assertThatThrownBy(() -> libraryService.addNewMember(librarian, librarian))
				.isInstanceOf(NewLibrarianException.class)
				.hasMessage("The new person does not have the role MEMBER")
				.extracting("errorCode").isEqualTo(4002);
	}

	@Test
	public void lendBook_ShouldLendBooksCorrectly() throws BookUnavailableException, BookNotFoundException, ForbiddenException {
		Book book = new Book(1,
				"TestBook",
				"TestAuthor",
				Genre.FANTASY,
				true);

		libraryService.lendBook(member, book);
		assertThat(member.getBooks()).hasSize(1);
		assertThat(member.getBooks()).first().isEqualTo(book);
		assertThat(libraryService.getAllBooks()).first().extracting(Book::isAvailable).isEqualTo(false);
	}

	@Test
	public void lendBook_ShouldthrowExceptions() {
		Book book3 = new Book(3,
				"TheThirdTestBook",
				"TheThirdTestAuthor",
				Genre.NON_FICTION,
				false);

		assertThatThrownBy(() -> libraryService.lendBook(librarian, book3))
				.isInstanceOf(ForbiddenException.class)
				.hasMessage("The calling person with id 1 is not allowed lend books.")
				.extracting("errorCode").isEqualTo(4003);

		assertThatThrownBy(() -> libraryService.lendBook(member, book3))
				.isInstanceOf(BookUnavailableException.class)
				.hasMessage("The book with id 3 is not available.")
				.extracting("errorCode").isEqualTo(4001);

		Book nonLibraryBook = new Book(4,
				"NotInLibrary",
				"UnknownAuthor",
				Genre.NON_FICTION,
				true);

		assertThatThrownBy(() -> libraryService.lendBook(member, nonLibraryBook))
				.isInstanceOf(BookNotFoundException.class)
				.hasMessage("The book with title NotInLibrary was not found in the library.")
				.extracting("errorCode").isEqualTo(4000);
	}
}
