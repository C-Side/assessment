package org.assessment.adesso.library.impl.books;

import org.assessment.adesso.library.impl.LibraryFacade;
import org.assessment.adesso.library.impl.books.type.Genre;

import java.util.Objects;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class Book {

	private int id;
	private String title;
	private String author;
	private Genre genre;
	private boolean available;

	public Book() {
	}

	public Book(int id, String title, String author, Genre genre, boolean available) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
		this.available = available;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return Objects.equals(title, book.title) &&
				Objects.equals(author, book.author) &&
				genre == book.genre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, author, genre);
	}
}
