package org.assessment.adesso.library.rest;

import org.assessment.adesso.library.api.LibraryService;
import org.assessment.adesso.library.impl.books.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : Klinghammer, Lukas; eXXcellent solutions
 * @version : 24.11.2021
 **/
@RestController
@RequestMapping("api/library")
public class LibraryRestService {

	private final LibraryService libraryService;

	@Autowired
	public LibraryRestService(LibraryService libraryService) {
		this.libraryService = libraryService;
	}

	@GetMapping("/books")
	public List<Book> getAllBooks() {
		return libraryService.getAllBooks();
	}
}
