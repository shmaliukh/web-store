package com.vshmaliukh.webstore.controllers;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
public class ImageController {

    final ImageService imageService;


    @GetMapping("/image/{id}")
    public void showProductImage(@PathVariable Long id,
                               HttpServletResponse response) throws IOException {
        response.setContentType("image/jpeg");
        Optional<Image> optionalImage = imageService.getImageById(id);
        if(optionalImage.isPresent()){
            Image image = optionalImage.get();
            byte[] imageData = image.getImageData();
            InputStream inputStream = new ByteArrayInputStream(imageData);
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

}
