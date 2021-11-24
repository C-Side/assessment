package org.assessment.adesso.library.impl.members;

import org.assessment.adesso.library.impl.books.Book;
import org.assessment.adesso.library.impl.members.type.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
public class Person {

	private int id;
	private String firstName;
	private String lastName;
	private List<Book> books = new ArrayList<>();
	private Role role;

	public Person() {
	}

	public Person(int id, String firstName, String lastName, Role role) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Person person = (Person) o;
		return id == person.id &&
				firstName.equals(person.firstName) &&
				lastName.equals(person.lastName) &&
				role == person.role;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, books, role);
	}
}
