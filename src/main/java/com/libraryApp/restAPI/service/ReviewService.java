package com.libraryApp.restAPI.service;


import com.libraryApp.restAPI.domain.Book;
import com.libraryApp.restAPI.domain.Review;
import com.libraryApp.restAPI.domain.User;
import com.libraryApp.restAPI.dto.ReviewDto;
import com.libraryApp.restAPI.repo.BookRepo;
import com.libraryApp.restAPI.repo.ReviewRepo;
import com.libraryApp.restAPI.repo.UserRepo;
import com.libraryApp.restAPI.security.jwt.JwtUser;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepo reviewRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    @Autowired
    public ReviewService(ReviewRepo reviewRepo, BookRepo bookRepo, UserRepo userRepo) {
        this.reviewRepo = reviewRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    public ReviewDto addReview(ReviewDto reviewDto) {
        Long authorId = reviewDto.getAuthorId();
        Long bookId = reviewDto.getBookId();

        Review reviewFromUser = reviewRepo.findByAuthor_IdAndBook_Id(authorId, bookId);
        if (reviewFromUser != null) {
            throw new RuntimeException("You have already written a review of this book");
        }

        Optional<Book> book = bookRepo.findById(bookId);
        Optional<User> author = userRepo.findById(authorId);
        if (!book.isPresent() || !author.isPresent()) {
            // use @Transactional? (юзер добавляет ревью и в это время админ удаляет книгу\юзера)
            throw new RuntimeException("Book or user not found");
        }


        Review review = new Review();
        BeanUtils.copyProperties(reviewDto, review, "id");
        review.setBook(book.get());
        review.setAuthor(author.get());
        reviewRepo.save(review);

        reviewDto.setBookName(book.get().getBookName());
        reviewDto.setUsername(author.get().getUsername());
        reviewDto.setId(review.getId());
        return reviewDto;
    }

    public boolean checkUserRights(JwtUser currentUser, Review review) {
        Long authorId = review.getAuthor().getId();
        if (currentUser.getId().equals(authorId)) {
            return true;
        }

        if (currentUser.isAdmin()) {
            return true;
        }

        return false;
    }

    public boolean updateReview(ReviewDto editedReview, Review dbReview) {
        Long editedReviewAuthorId = editedReview.getAuthorId();
        Long editedReviewBookId = editedReview.getBookId();

        Long dbReviewAuthorId = dbReview.getAuthor().getId();
        Long dbReviewBookId = dbReview.getBook().getId();

        if (dbReviewAuthorId.equals(editedReviewAuthorId) && dbReviewBookId.equals(editedReviewBookId)) {
            BeanUtils.copyProperties(editedReview, dbReview, "id");
            reviewRepo.save(dbReview);

            return true;
        }

        return false;
    }


    public void deleteReview(Review review) {
        reviewRepo.delete(review);
    }


}
