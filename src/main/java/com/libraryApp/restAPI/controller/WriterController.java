package com.libraryApp.restAPI.controller;

import com.libraryApp.restAPI.domain.Writer;
import com.libraryApp.restAPI.service.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class WriterController {

    private final WriterService writerService;

    @Autowired
    public WriterController(WriterService writerService) {
        this.writerService = writerService;
    }


    @GetMapping("/writers")
    public ResponseEntity<List<Writer>> getWriters() {
        return new ResponseEntity<>(writerService.getWriters(), HttpStatus.OK);
    }

    @GetMapping("/writers/{writer:[\\d]+}")
    public ResponseEntity<Writer> getWriter(@PathVariable Writer writer) {
        if (writer != null) {
            return new ResponseEntity<>(writer, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping("/writers")
    public ResponseEntity<Writer> createWriter(@RequestBody Writer writer) {
        /* VALIDATION */

        if (writerService.addWriter(writer)) {
            return new ResponseEntity<>(writer, HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/writers/{currentWriter:[\\d]+}")
    public ResponseEntity<Writer> updateWriter(
            @PathVariable Writer currentWriter,
            @RequestBody Writer writer
    ) {
        /* VALIDATION */

        if (currentWriter != null) {
            if (writerService.updateWriter(writer, currentWriter)) {
                return new ResponseEntity<>(currentWriter, HttpStatus.OK);
            }
        }

        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/writers/{writer:[\\d]+}")
    public ResponseEntity deleteWriter(@PathVariable Writer writer) {
        if (writer != null) {
            writerService.deleteWriter(writer);
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }


}
