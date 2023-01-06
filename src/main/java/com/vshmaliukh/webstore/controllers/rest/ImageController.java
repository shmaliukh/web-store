package com.vshmaliukh.webstore.controllers.rest;

import com.vshmaliukh.webstore.ImageUtil;
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

import java.util.Optional;

@Slf4j
@RestController("/image")
@AllArgsConstructor
public class ImageController {

    final ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> showProductImage(@PathVariable Long id) {
        Optional<Image> optionalImage = imageService.getImageById(id);
        if (optionalImage.isPresent()) {
            Image itemImage = optionalImage.get();
            byte[] compressedImageData = itemImage.getImageData();
            byte[] decompressedImage = ImageUtil.decompressImage(compressedImageData);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(decompressedImage);
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        Optional<Image> optionalImage = imageService.getImageById(id);
        if (optionalImage.isPresent()) {
            Image itemImage = optionalImage.get();
            imageService.deleteImage(itemImage);
            return ResponseEntity.ok().build();
        }
        log.warn("not found image by '{}' id to delete", id);
        return ResponseEntity.badRequest().build();
    }

}
