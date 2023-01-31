package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import com.vshmaliukh.webstore.repositories.ItemImageRepository;
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
public class ImageService implements EntityValidator<Image> {

    final ImageRepository imageRepository;
    final ItemImageRepository itemImageRepository;

    public void saveImage(Image image) {
        if (isValidEntity(image)) {
            imageRepository.save(image);
            log.info("saved image: {}", image);
        } else {
            log.error("image not saved // invalid image");
        }
    }

    public Optional<Image> buildImageFromFile(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String filename = file.getOriginalFilename();
                String fileContentType = file.getContentType();
                byte[] fileBytes = file.getBytes();

                Image image = new Image();
                image.setName(filename);
                image.setType(fileContentType);
                image.setImageData(fileBytes);

                return Optional.of(image);
            } else {
                log.error("problem to build image out of the file"
                        + (file == null ? " // file is NULL" : " // file is empty"));
            }
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
        return Optional.empty();
    }

    public Optional<ItemImage> formItemImageFromFile(Item item, MultipartFile file) {
        Optional<Image> optionalImage = buildImageFromFile(file);
        if (optionalImage.isPresent()) {
            ItemImage itemImage = new ItemImage(optionalImage.get(), item);
            return Optional.of(itemImage);
        }
        log.warn("problem to generate  image entity from file '{}'", file);
        return Optional.empty();
    }

    public Optional<Image> findImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<ItemImage> findImageListByItem(Item item) {
        List<ItemImage> imagesByItem = itemImageRepository.findImagesByItem(item);
        return imagesByItem != null
                ? imagesByItem
                : Collections.emptyList();
    }

    public void deleteImage(Image image) {
        if (isValidEntity(image)) {
            imageRepository.delete(image);
            log.info("deleted '{}' image", image);
        }
        log.warn("image not deleted // invalid image");
    }

}
