package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class ImageController {

    final ImageService imageService;

//    @GetMapping("/image/{id}")
//    public ResponseEntity<byte[]> showProductImage(@PathVariable Long id) {
//        Optional<Image> optionalImage = imageService.getImageById(id);
//        if (optionalImage.isPresent()) {
//            Image image = optionalImage.get();
//            byte[] imageData = image.getImageData();
//            final HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.IMAGE_PNG);
////            headers.setContentType(MediaType.IMAGE_JPEG);
////            headers.setContentType(MediaType.IMAGE_GIF);
//            return new ResponseEntity (imageData, headers, HttpStatus.OK);
////            return ResponseEntity.ok()
////                    .contentType(MediaType.IMAGE_JPEG)
////                    .contentType(MediaType.IMAGE_PNG)
////                    .contentType(MediaType.IMAGE_GIF)
////                    .body(imageData);
//        }
//        return ResponseEntity.badRequest().build();
//    }


    @ResponseBody
    @GetMapping("/image/{id}")
    void showImage(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Optional<Image> optionalImage = imageService.getImageById(id);
        if (optionalImage.isPresent()) {

            Image image = optionalImage.get();
            byte[] imageData = image.getImageData();

            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            response.getOutputStream().write(imageData);
            response.getOutputStream().close();
        }
    }

}
