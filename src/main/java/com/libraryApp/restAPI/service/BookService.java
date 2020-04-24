package com.libraryApp.restAPI.service;


import com.libraryApp.restAPI.domain.Book;
import com.libraryApp.restAPI.domain.Writer;
import com.libraryApp.restAPI.repo.BookRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.List;

@Service
public class BookService {

    @Value("${upload.path}")
    private String uploadPath;

    private final BookRepo bookRepo;
    private final ImageService imageService;

    @Autowired
    public BookService(BookRepo bookRepo, ImageService imageService) {
        this.bookRepo = bookRepo;
        this.imageService = imageService;
    }


    public List<Book> getBooks() {
        return bookRepo.findAll();
    }

    public boolean addBook(Book book) {
        if (isBookExists(book)) {
            imageService.deleteFile(book.getFilename());
            return false;
        }

        bookRepo.save(book);
        return true;
    }

    public boolean updateBook(Book editedBook, Book dbBook) {
        boolean isNewBookName = !dbBook.getBookName().equals(editedBook.getBookName());
        boolean isNewWriter = !dbBook.getWriter().equals(editedBook.getWriter());
        boolean isNewPosterFile = !dbBook.getFilename().equals(editedBook.getFilename());

        if (isNewBookName || isNewWriter) {
            if (isBookExists(editedBook)) {
                // удаляем постер, т.к. не обновляем книгу в DB
                if (isNewPosterFile) {
                    imageService.deleteFile(editedBook.getFilename());
                }
                return false;
            }
        }

        if (isNewPosterFile) {
            imageService.deleteFile(dbBook.getFilename());
            dbBook.setFilename(editedBook.getFilename());
        }

        BeanUtils.copyProperties(editedBook, dbBook, "id", "filename", "reviews");
        bookRepo.save(dbBook);
        return true;
    }

    private boolean isBookExists(Book book) {
        Book bookFromDb = bookRepo.findByBookNameAndWriter(book.getBookName(), book.getWriter());
        return bookFromDb != null;
    }

    public void deleteBook(Book book) {
        imageService.deleteFile(book.getFilename());
        bookRepo.delete(book);
    }

}
