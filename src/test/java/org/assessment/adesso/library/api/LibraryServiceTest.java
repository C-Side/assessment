package org.assessment.adesso.library.api;

import org.assessment.adesso.common.exceptions.BusinessException;
import org.assessment.adesso.common.exceptions.BusinessExceptionType;
import org.assessment.adesso.library.impl.LibraryFacade;
import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.books.type.Genre;
import org.assessment.adesso.library.impl.members.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThatThrownBy;

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

	@AfterEach
	public void discardTestData() {
		libraryService.getAllBooks().clear();
		librarian.getBooks().clear();
		member.getBooks().clear();
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
				.isInstanceOf(BusinessException.class)
				.hasMessage("The calling person with id 2 is not allowed to add new books to the library.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.FORBIDDEN.name(), 4003);
	}

	@Test
	public void addNewMembersTest_ShouldThrowExceptions() {
		assertThatThrownBy(() -> libraryService.addNewMember(librarian, librarian))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The new person does not have the role MEMBER")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.NEW_LIBRARIAN.name(), 4002);
	}

	@Test
	public void lendBook_ShouldLendBooksCorrectly() throws BusinessException {
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
	public void lendBook_ShouldThrowTooManyBooksException() {
		Book book = new Book(1,
				"TestBook",
				"TestAuthor",
				Genre.FANTASY,
				true);

		for (int i = 0; i < 7; i++) {
			member.getBooks().add(book);
		}

		assertThatThrownBy(() -> libraryService.lendBook(member, book))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The person with id 2 already lent seven books.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.TOO_MANY_BOOKS.name(), 4004);
	}

	@Test
	public void lendBook_ShouldThrowExceptions() {
		Book book3 = new Book(3,
				"TheThirdTestBook",
				"TheThirdTestAuthor",
				Genre.NON_FICTION,
				false);

		assertThatThrownBy(() -> libraryService.lendBook(member, book3))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The book with id 3 is not available.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.BOOK_UNAVAILABLE.name(), 4001);

		Book nonLibraryBook = new Book(4,
				"NotInLibrary",
				"UnknownAuthor",
				Genre.NON_FICTION,
				true);

		assertThatThrownBy(() -> libraryService.lendBook(member, nonLibraryBook))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The book with title NotInLibrary was not found in the library.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.BOOK_NOT_FOUND.name(), 4000);
	}

	@Test
	public void returnBook_shouldReturnBook() throws BusinessException {
		Book book3 = new Book(3,
				"TheThirdTestBook",
				"TheThirdTestAuthor",
				Genre.NON_FICTION,
				false);
		member.getBooks().add(book3);
		libraryService.returnBook(member, book3);
		assertThat(member.getBooks()).hasSize(0);
		assertThat(libraryService.getAllBooks()).first().extracting(Book::isAvailable).isEqualTo(true);
	}

	@Test
	public void returnBook_shouldThrowExceptions() {
		Book notPossessedBook = new Book(5,
				"notPossessedBook",
				"UnknownAuthor",
				Genre.NON_FICTION,
				true);
		libraryService.getAllBooks().add(notPossessedBook);
		assertThatThrownBy(() -> libraryService.returnBook(member, notPossessedBook))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The book with title notPossessedBook was never in the possession of the person.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.BOOK_NOT_IN_POSSESSION.name(), 4005);

		Book nonLibraryBook = new Book(4,
				"NotInLibrary",
				"UnknownAuthor",
				Genre.NON_FICTION,
				true);
		member.getBooks().add(nonLibraryBook);
		assertThatThrownBy(() -> libraryService.returnBook(member, nonLibraryBook))
				.isInstanceOf(BusinessException.class)
				.hasMessage("The book with title NotInLibrary was not found in the library.")
				.extracting("exceptionType")
				.extracting("name", "errorCode").contains(BusinessExceptionType.BOOK_NOT_FOUND.name(), 4000);
	}

	@Test
	public void removeBookFromLibrary_shouldRemoveBook() throws BusinessException {
		Book book1 = new Book(1,
				"TestBook",
				"TestAuthor",
				Genre.FANTASY,
				true);
		libraryService.removeBookFromLibrary(librarian, book1);
		assertThat(libraryService.getAllBooks()).hasSize(2);
		assertThat(libraryService.getAllBooks().remove(book1)).isFalse();
	}
}
