package com.libraryApp.restAPI.controller;


import com.libraryApp.restAPI.domain.Book;
import com.libraryApp.restAPI.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }


    @GetMapping("books")
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookService.getBooks(), HttpStatus.OK);
    }

    @GetMapping("books/{book:[\\d]+}")
    public ResponseEntity<Book> getBook(@PathVariable Book book) {
        if (book != null) {
            return new ResponseEntity<>(book, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        /* VALIDATION?! */

        if (bookService.addBook(book)) {
            return new ResponseEntity<>(book, HttpStatus.CREATED);
        }


        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/books/{currentBook:[\\d]+}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Book currentBook,
            @RequestBody Book book
    ) {
        /* VALIDATION */

        if (currentBook != null) {
            if (bookService.updateBook(book, currentBook)) {
                return new ResponseEntity<>(currentBook, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/books/{book:[\\d]+}")
    public ResponseEntity deleteBook(@PathVariable Book book) {
        if (book != null) {
            bookService.deleteBook(book);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }


}
