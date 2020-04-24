package com.libraryApp.restAPI.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;


    public String loadFile(MultipartFile file) throws IOException {
        boolean isImageFile = ImageIO.read(file.getInputStream()) != null;

        if (isImageFile) {
            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            file.transferTo(new File(uploadPath + "/" + resultFilename));

            return resultFilename;
        }

        throw new IOException();
    }

    public String getFile(String filename) throws IOException {
        File image = new File(uploadPath + "/" + filename);
        FileInputStream fileInputStream = new FileInputStream(image);
        byte[] bytes = new byte[(int) image.length()];

        fileInputStream.read(bytes);
        String encodeBase64 = Base64.getEncoder().encodeToString(bytes);
        String extension = FilenameUtils.getExtension(image.getName());

        fileInputStream.close();

        return "data:image/" + extension + ";base64," + encodeBase64;
    }

    public void deleteFile(String filename) {
        String path = uploadPath + "/" + filename;
        File file = new File(path);
        file.delete();
    }


}
