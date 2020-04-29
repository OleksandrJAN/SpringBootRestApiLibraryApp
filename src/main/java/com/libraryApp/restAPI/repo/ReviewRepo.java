package com.libraryApp.restAPI.repo;

import com.libraryApp.restAPI.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    Review findByAuthor_IdAndBook_Id(Long userId, Long bookId);
}
