package com.vshmaliukh.webstore.services;

import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.Item;
import com.vshmaliukh.webstore.repositories.ItemRepositoryProvider;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.BaseItemRepository;
import com.vshmaliukh.webstore.repositories.literature_items_repositories.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class ItemService implements EntityValidator<Item> {

    @Getter
    final ItemRepository itemRepository;
    final ItemRepositoryProvider itemRepositoryProvider;
    final ImageService imageService;

    public void addImageToItem(Integer itemId, MultipartFile file) {
        Optional<Item> optionalItem = readItemById(itemId);
        if (optionalItem.isPresent()) {
            Item item = optionalItem.get();
            Optional<ItemImage> optionalImage = imageService.formItemImageFromFile(item, file);
            if (optionalImage.isPresent()) {
                ItemImage itemImageToSave = optionalImage.get();
                imageService.saveImage(itemImageToSave);
            }
        } else {
            log.warn("image not added to item with '{}'", itemId);
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
        ItemRepository allItemRepository = itemRepositoryProvider.getAllItemRepository();
        return allItemRepository.findById(itemId);
    }

    // TODO implement read items via repository
    public List<Item> readItemsAvailableToBuy() {
        ItemRepository allItemRepository = itemRepositoryProvider.getAllItemRepository();
//        return allItemRepository.findAllByQuantityGreaterThanEqualAndAvailableInStoreEquals(1, true);
        return allItemRepository.findAll().stream()
                .filter(item -> item.getAvailableToBuyQuantity() > 0)
                .collect(Collectors.toList());
    }

    public <T extends Item> void saveItem(T item) {
        BaseItemRepository<T> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
            if (item.getCurrentQuantity() < 1) {
                item.setAvailableInStore(false);
            }
            if (item.getId() != null) {
                List<ItemImage> imageListByItem = imageService.findImageListByItem(item);
                item.setImageList(imageListByItem);
            }
            baseItemRepository.save(item);
        } else {
            log.warn("problem to save '{}' item , repository not found", item);
        }
    }

    public boolean isItemSaved(Item item) {
        BaseItemRepository<? extends Item> baseItemRepository = itemRepositoryProvider.getItemRepositoryByItemClassType(item);
        if (baseItemRepository != null) {
            List<? extends Item> allItemList = baseItemRepository.findAll();
            return allItemList.contains(item);
        } else {
            log.warn("problem to check if the item is saved // item '{}'", item);
        }
        return false;
    }

    public List<? extends Item> readAllItemsByTypeName(String itemTypeName) {
        BaseItemRepository<? extends Item> itemRepositoryByItemTypeNameByType = itemRepositoryProvider.getItemRepositoryByItemClassName(itemTypeName);
        if (itemRepositoryByItemTypeNameByType != null) {
            return itemRepositoryByItemTypeNameByType.findAll();
        }
        log.warn("problem to read all items by type name // not found repository // itemTypeName: {}", itemTypeName);
        return Collections.emptyList();
    }

    public <T extends Item> void deleteItem(T item) {
        if (isValidEntity(item)) {
            itemRepositoryProvider.getAllItemRepository().delete(item);
        } else {
            log.error("problem to delete item: '{}' // invalid item", item);
        }
    }

    public Set<String> readStatusNameSet() {
        // TODO implement enum
        return new HashSet<>(
                Arrays.asList(
                        "In stock",
                        //You’re currently accepting orders for this product and can fulfill the purchase request. You’re certain that the product will ship (or be in-transit to the customer) in a timely manner because it's available for sale. You can deliver the product to all of the locations that you support in your product data and account shipping settings.
                        "Out of stock",
                        //You’re not currently accepting orders for this product, or the product is not available for purchase or needs to be backordered.
                        "Preorder",
                        //You’re currently taking orders for this product, but it’s not yet been released for sale. You're required to provide the availability date [availability_date] attribute to indicate the day that the product becomes available for delivery.
                        "Backorder"
                        //The product is not available at the moment, but you’re accepting orders and it'll be shipped as soon as it becomes available again. You're required to provide the availability date [availability_date] attribute to indicate the day that the product becomes available for delivery.
                )
        );
    }

    public Set<String> readTypeNameSet() {
        return Collections.unmodifiableSet(itemRepositoryProvider.readTypeNameSet());
    }

    public BaseItemRepository getItemRepositoryByItemTypeName(String itemType) {
        // TODO solve 'Raw use of parameterized class 'BaseItemRepository''
        BaseItemRepository itemRepository = itemRepositoryProvider.getItemRepositoryByItemClassName(itemType.toLowerCase());
        if (itemRepository != null) {
            return itemRepository;
        }
        return itemRepositoryProvider.getAllItemRepository();
    }

}
