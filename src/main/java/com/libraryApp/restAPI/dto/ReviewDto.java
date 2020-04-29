package com.libraryApp.restAPI.dto;

import com.libraryApp.restAPI.domain.Assessment;
import com.libraryApp.restAPI.domain.Review;
import org.hibernate.validator.constraints.Length;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class ReviewDto {

    private Long id;

    @NotBlank
    @Length(max = 2047)
    private String text;

    @Enumerated(EnumType.STRING)
    private Assessment assessment;

    @Positive
    private Long authorId;
    private String username;

    @Positive
    private Long bookId;
    private String bookName;

    public ReviewDto(){}

    public ReviewDto(Review review) {
        this.id = review.getId();
        this.text = review.getText();
        this.assessment = review.getAssessment();
        this.authorId = review.getAuthor().getId();
        this.username = review.getAuthor().getUsername();
        this.bookId = review.getBook().getId();
        this.bookName = review.getBook().getBookName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Assessment getAssessment() {
        return assessment;
    }

    public void setAssessment(Assessment assessment) {
        this.assessment = assessment;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

}
