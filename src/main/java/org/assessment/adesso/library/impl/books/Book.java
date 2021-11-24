package org.assessment.adesso.library.impl.books;

import org.assessment.adesso.library.impl.books.type.Genre;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class Book {

	private int id;
	private String title;
	private String author;
	private Genre genre;

	public Book() {
	}

	public Book(int id, String title, String author, Genre genre) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.genre = genre;
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
}
