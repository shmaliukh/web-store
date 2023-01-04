package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.ImageUtil;
import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ImageService {

    final ImageRepository imageRepository;

    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    public Optional<Image> formImageFromFile(Item item, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileContentType = file.getContentType();
            byte[] compressedImage = ImageUtil.compressImage(file.getBytes());
            return Optional.ofNullable(Image.builder()
                    .item(item)
                    .name(filename)
                    .type(fileContentType)
                    .imageData(compressedImage)
                    .build());
        } catch (IOException e) {
            log.error("problem to save '{}' image to database", file);
            return Optional.empty();
        }
    }

    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<Image> findImageListByItem(Item item) {
        List<Image> imagesByItem = imageRepository.findImagesByItem(item);
        return imagesByItem != null
                ? imagesByItem
                : Collections.emptyList();
    }

}
