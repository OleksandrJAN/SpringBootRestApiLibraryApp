package com.libraryApp.restAPI.service;

import com.libraryApp.restAPI.domain.Writer;
import com.libraryApp.restAPI.repo.WriterRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WriterService {

    private final WriterRepo writerRepo;

    @Autowired
    public WriterService(WriterRepo writerRepo) {
        this.writerRepo = writerRepo;
    }


    public List<Writer> getWriters() {
        return writerRepo.findAll();
    }


    public boolean addWriter(Writer writer) {
        if (isWriterExists(writer)) {
            return false;
        }

        writerRepo.save(writer);
        return true;
    }

    public boolean updateWriter(Writer editedWriter, Writer dbWriter) {
        if (isWriterExists(editedWriter)) {
            return false;
        }

        BeanUtils.copyProperties(editedWriter, dbWriter, "id", "books");
        writerRepo.save(dbWriter);
        return true;
    }

    private boolean isWriterExists(Writer writer) {
        Writer writerFromDb = writerRepo.findByFirstNameAndLastName(writer.getFirstName(), writer.getLastName());
        return writerFromDb != null;
    }


    public void deleteWriter(Writer writer) {
        writerRepo.delete(writer);
    }
}
