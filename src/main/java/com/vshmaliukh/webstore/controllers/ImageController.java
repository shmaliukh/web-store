package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class ImageController {

    final ImageService imageService;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable Long id) {
        Optional<Image> optionalImage = imageService.getImageById(id);
        if (optionalImage.isPresent()) {
            Image image = optionalImage.get();
            byte[] imageData = image.getImageData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageData);
        }
        return ResponseEntity.badRequest().build();
    }

}
