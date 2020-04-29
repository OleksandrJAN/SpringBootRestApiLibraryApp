package com.libraryApp.restAPI.controller;


import com.libraryApp.restAPI.domain.Book;
import com.libraryApp.restAPI.domain.Review;
import com.libraryApp.restAPI.domain.User;
import com.libraryApp.restAPI.dto.ReviewDto;
import com.libraryApp.restAPI.security.jwt.JwtUser;
import com.libraryApp.restAPI.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /*ALL REVIEWS*/

    @GetMapping("/books/{book:[\\d]+}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable(value = "book") Book book) {
        if (book != null) {
            List<ReviewDto> reviews = book.getReviews().stream().map(
                    review -> new ReviewDto(review)
            ).collect(Collectors.toList());
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/users/{user:[\\d]+}/reviews")
    public ResponseEntity<List<ReviewDto>> getReviews(@PathVariable(value = "user") User userProfile) {
        if (userProfile != null) {
            List<ReviewDto> reviews = userProfile.getReviews().stream().map(
                    review -> new ReviewDto(review)
            ).collect(Collectors.toList());
            return new ResponseEntity<>(reviews, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/reviews")
    public ResponseEntity createReview(
            @Valid @RequestBody ReviewDto review,
            @AuthenticationPrincipal JwtUser user
    ) {
        /* VALIDATION */
        if (!user.getId().equals(review.getAuthorId())) {
            return ResponseEntity.badRequest().body("Incorrect user data");
        }

        if (review.getBookId() == null) {
            return ResponseEntity.badRequest().body("Incorrect book data");
        }


        try {
            ReviewDto createdReview = reviewService.addReview(review);
            return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }


    @PutMapping("/reviews/{currentReview:[\\d]+}")
    public ResponseEntity updateReview(
            @Valid @RequestBody ReviewDto editedReview,
            @PathVariable("currentReview") Review currentReview,
            @AuthenticationPrincipal JwtUser user
    ) {
        /* VALIDATION */

        if (currentReview != null) {

            if (reviewService.checkUserRights(user, currentReview)) {

                if (reviewService.updateReview(editedReview, currentReview)) {
                    return ResponseEntity.ok(editedReview);
                }
                return ResponseEntity.badRequest().body("Incorrect book or user data");
            }

            return ResponseEntity.badRequest().body("Not enough rights");
        }

        return ResponseEntity.badRequest().body("Review not found");
    }


    @DeleteMapping("/reviews/{review:[\\d]+}")
    public ResponseEntity deleteReview(
            @PathVariable Review review,
            @AuthenticationPrincipal JwtUser user
    ) {
        if (review != null) {

            if (reviewService.checkUserRights(user, review)) {
                reviewService.deleteReview(review);
                return new ResponseEntity(HttpStatus.OK);
            }

            return ResponseEntity.badRequest().body("Not enough rights");
        }

        return ResponseEntity.badRequest().body("Review not found");
    }


}
