package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.ItemStatus;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService implements EntityValidator<Item> {

    @Getter
    final ItemRepository itemRepository;
    final ItemRepositoryProvider itemRepositoryProvider;
    final ImageService imageService;

    public void addImageToItem(Item item, MultipartFile file) {
        if (isValidEntity(item)) {
            Optional<ItemImage> optionalImage = imageService.formItemImageFromFile(item, file);
            if (optionalImage.isPresent()) {
                ItemImage itemImageToSave = optionalImage.get();
                imageService.saveImage(itemImageToSave);
                log.info("successfully added image to item // item: '{}', image: '{}'", item, itemImageToSave);
            } else {
                log.error("problem to add image to item // problem to generate image for item: '{}'", item);
            }
        } else {
            log.error("problem to add image to item // invalid item: '{}'", item);
        }
    }

    public void changeItemImage(Integer itemId, Long imageId, MultipartFile file) {
        Optional<Item> optionalItem = readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            Optional<ItemImage> optionalImage = imageService.formItemImageFromFile(item, file);
            if (optionalImage.isPresent()) {
                ItemImage itemImageToSave = optionalImage.get();
                itemImageToSave.setId(imageId);
                imageService.saveImage(itemImageToSave);
            }
        } else {
            log.warn("image not changed // item id: '{}' // image id: '{}'", itemId, imageId);
        }
    }

    public Optional<Item> readItemById(Integer itemId) {
        if (itemId != null && itemId > 0) {
            return itemRepository.findById(itemId);
        } else {
            log.error("problem to read item by id"
                    + (itemId == null ? " // item id is NULL" : " // item id < 1"));
            return Optional.empty();
        }
    }

    public <T extends Item> void saveItem(T item) {
        if (isValidEntity(item)) {
//            if (item.getCurrentQuantity() < 1) {
//                item.setAvailableInStore(false);
//            }
            if (item.getId() != null) {
                List<ItemImage> imageListByItem = imageService.findImageListByItem(item);
                item.setImageList(imageListByItem);
            }
            itemRepository.save(item);
            log.info("item successfully saved // item: '{}'", item);
        } else {
            log.error("problem to save item // invalid item");
        }
    }

    public boolean isItemSaved(Item item) {
        if (isValidEntity(item)) {
            Integer id = item.getId();
            if (id != null && id > 0) {
                return itemRepository.existsById(id);
            } else {
                log.error("problem to check if the item is saved"
                        + (id == null ? " // id is NULL" : " // id < 1"));
            }
        } else {
            log.error("problem to check if the item is saved // invalid item: '{}'", item);
        }
        return false;
    }

    public List<? extends Item> readAllItemsByTypeName(String itemTypeName) {
        BaseItemRepository<? extends Item> itemRepositoryByItemTypeNameByType = itemRepositoryProvider.getItemRepositoryByItemClassName(itemTypeName);
        if (itemRepositoryByItemTypeNameByType != null) {
            return Collections.unmodifiableList(itemRepositoryByItemTypeNameByType.findAll());
        }
        log.error("problem to read all items by type name // not found repository // itemTypeName: {}", itemTypeName);
        return Collections.emptyList();
    }

    public <T extends Item> void deleteItem(T item) {
        if (isValidEntity(item)) {
            itemRepository.delete(item);
            log.info("item successfully deleted // item: '{}'", item);
        } else {
            log.error("problem to delete item: '{}' // invalid item", item);
        }
    }

    public List<String> readStatusNameList() {
        return Collections.unmodifiableList(ItemStatus.getStatusNameList());
    }

    public Set<String> readTypeNameSet() {
        return Collections.unmodifiableSet(itemRepositoryProvider.readTypeNameSet());
    }

    public BaseItemRepository getItemRepositoryByItemTypeName(String itemTypeStr) {
        if (StringUtils.isNotBlank(itemTypeStr)) {
            BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(itemTypeStr.toLowerCase());
            if (itemRepository != null) {
                return itemRepository;
            } else {
                log.warn("problem to find repository by type // not found repository by type: '{}' // return all item repository", itemTypeStr);
            }
        } else {
            log.warn("problem to find repository by type // item type str is blank // return all item repository");
        }
        return itemRepositoryProvider.getAllItemRepository();
    }

}
