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
import java.util.function.Supplier;

@Slf4j
@Service
@AllArgsConstructor
public class ImageService {

    final ImageRepository imageRepository;
    final ItemImageRepository itemImageRepository;

    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    public Optional<Image> buildImageFromFile(MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileContentType = file.getContentType();
            byte[] compressedImage = file.getBytes();

            Image image = new Image();
            image.setName(filename);
            image.setType(fileContentType);
            image.setImageData(compressedImage);

            return Optional.of(image);
        } catch (IOException ioe) {
            log.error(ioe.getMessage(), ioe);
        }
        return Optional.empty();
    }

    public Optional<ItemImage> formItemImageFromFile(Item item, MultipartFile file) {
        Optional<Image> optionalImage = buildImageFromFile(file);
        if (optionalImage.isPresent()) {
            // TODO refactor (not use cast)
            ItemImage itemImage = (ItemImage) optionalImage.get();
            itemImage.setItem(item);
            return Optional.of(itemImage);
        }
        log.warn("problem to generate '{}' file to image entity", file);
        return Optional.empty();
    }

    public Optional<Image> getImageById(Long id) {
        return imageRepository.findById(id);
    }

    public List<ItemImage> findImageListByItem(Item item) {
        List<ItemImage> imagesByItem = itemImageRepository.findImagesByItem(item);
        return imagesByItem != null
                ? imagesByItem
                : Collections.emptyList();
    }

    public void deleteImage(Image image) {
        if (image != null) {
            imageRepository.delete(image);
            log.info("deleted '{}' image", image);
        }
        log.warn("image not deleted // image == NULL");
    }

    public Supplier<Image> getDefaultImage() {
        return null;
    }
}
