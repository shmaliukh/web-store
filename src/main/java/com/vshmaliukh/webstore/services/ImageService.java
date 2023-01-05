package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.ImageUtil;
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
public class ImageService {

    final ImageRepository imageRepository;
    final ItemImageRepository itemImageRepository;

    public void saveImage(Image image) {
        imageRepository.save(image);
    }

    public Optional<ItemImage> formItemImageFromFile(Item item, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            String fileContentType = file.getContentType();
            byte[] compressedImage = ImageUtil.compressImage(file.getBytes());

            ItemImage itemImage = new ItemImage();
            itemImage.setItem(item);
            itemImage.setName(filename);
            itemImage.setType(fileContentType);
            itemImage.setImageData(compressedImage);

            return Optional.of(itemImage);
        } catch (IOException e) {
            log.error("problem to save '{}' image to database", file);
            return Optional.empty();
        }
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

    public void deleteImage(Image Image) {
        if (Image != null) {
            imageRepository.delete(Image);
            log.info("deleted '{}' image", Image);
        }
        log.warn("image not deleted // image == NULL");
    }

}
