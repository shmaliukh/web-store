package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Image;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {

    List<Image> findImagesByItem(Item item);

}
