package com.libraryApp.restAPI.controller;

import com.libraryApp.restAPI.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }


    /* return new filename */
    @PostMapping("image")
    public ResponseEntity<String> fileUpload(@RequestParam("file") MultipartFile file) {
        try {
            String filename = imageService.loadFile(file);
            return new ResponseEntity<>(filename, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }
    }

    @GetMapping("image/{filename:.+}")
    public ResponseEntity<String> getFile(@PathVariable String filename) {
        try {
            String image = imageService.getFile(filename);
            return new ResponseEntity<>(image, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
