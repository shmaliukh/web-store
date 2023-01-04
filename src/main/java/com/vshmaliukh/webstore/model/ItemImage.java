package com.vshmaliukh.webstore.model;


import com.vshmaliukh.webstore.model.items.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "itemImage")
public class ItemImage extends Image {

    @ManyToOne
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemImage)) return false;
        if (!super.equals(o)) return false;

        ItemImage itemImage = (ItemImage) o;

        return getItem() != null ? getItem().equals(itemImage.getItem()) : itemImage.getItem() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
        return result;
    }
}
