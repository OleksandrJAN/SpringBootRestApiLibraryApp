package com.libraryApp.restAPI.repo;

import com.libraryApp.restAPI.domain.Writer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepo extends JpaRepository<Writer, Long> {
    Writer findByFirstNameAndLastName(String firstName, String lastName);
}
