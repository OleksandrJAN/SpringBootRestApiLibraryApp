package com.libraryApp.restAPI.repo;


import com.libraryApp.restAPI.domain.Book;
import com.libraryApp.restAPI.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long> {
    Book findByBookNameAndWriter(String bookName, Writer writer);
}
