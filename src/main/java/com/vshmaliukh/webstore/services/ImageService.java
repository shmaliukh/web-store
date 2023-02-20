package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ImageRepository;
import com.vshmaliukh.webstore.repositories.ItemImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ImageService implements EntityValidator<Image> {

    private final ImageRepository imageRepository;
    private final ItemImageRepository itemImageRepository;

    public ImageService(ImageRepository imageRepository, ItemImageRepository itemImageRepository) {
        this.imageRepository = imageRepository;
        this.itemImageRepository = itemImageRepository;
    }

    public Optional<Image> saveImage(Image image) {
        if (isValidEntity(image)) {
            imageRepository.save(image);
            log.info("saved image: {}", image);
            return Optional.of(image);
        } else {
            log.error("image not saved // invalid image: {}", image);
            return Optional.empty();
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
            // TODO create test to catch 'IOException' messages
            log.error("problem to build image out of the file // IOException");
            log.error(ioe.getMessage(), ioe);
        }
        return Optional.empty();
    }

    public Optional<ItemImage> formItemImageFromFile(Item item, MultipartFile file) {
        Optional<Image> optionalImage = buildImageFromFile(file);
        if (item != null && optionalImage.isPresent()) {
            ItemImage itemImage = new ItemImage(optionalImage.get(), item);
            return Optional.of(itemImage);
        }
        log.warn("problem to generate item image"
                + (item == null ? " // item is NULL" : " // problem to generate image out of file"));
        return Optional.empty();
    }

    public Optional<Image> readImageById(Long id) {
        if (id != null && id > 0L) {
            return imageRepository.findById(id);
        }
        log.warn("problem to find image by id"
                + (id == null ? " // id is NULL" : " // id < 1"));
        return Optional.empty();
    }

    public Optional<ItemImage> readItemImageById(Long id) {
        if (id != null && id > 0L) {
            return itemImageRepository.findById(id);
        }
        log.warn("problem to find item image by id"
                + (id == null ? " // id is NULL" : " // id < 1"));
        return Optional.empty();
    }

    public List<ItemImage> readImageListByItem(Item item) {
        List<ItemImage> imagesByItem = item != null ? itemImageRepository.findImagesByItem(item) : null;
        return imagesByItem != null
                ? imagesByItem
                : Collections.emptyList();
    }

    public void deleteImage(Image image) {
        if (isValidEntity(image)) {
            imageRepository.delete(image);
            log.info("deleted image: '{}'", image);
        } else {
            log.warn("image not deleted // invalid image");
        }
    }

}
