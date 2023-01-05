package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findImagesByItem(Item item);

}
