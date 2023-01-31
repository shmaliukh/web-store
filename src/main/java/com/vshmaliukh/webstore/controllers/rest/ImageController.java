package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class ImageController {

    final ImageService imageService;

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable @Min(1) Long id) {
        Optional<Image> optionalImage = imageService.findImageById(id);
        if (optionalImage.isPresent()) {
            Image itemImage = optionalImage.get();
            byte[] imageData = itemImage.getImageData();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageData);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable @Min(1) Long id) {
        Optional<Image> optionalImage = imageService.findImageById(id);
        if (optionalImage.isPresent()) {
            Image itemImage = optionalImage.get();
            imageService.deleteImage(itemImage);
            return ResponseEntity.ok().build();
        }
        log.warn("problem to delete image by '{}' // not found image", id);
        return ResponseEntity.badRequest().build();
    }

}
