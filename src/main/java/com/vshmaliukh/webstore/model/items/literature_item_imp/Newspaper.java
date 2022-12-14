package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshmaliukh.webstore.model.items.LiteratureItem;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = NEWSPAPER_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN
        })})
public class Newspaper extends LiteratureItem {

    @JsonCreator
    public Newspaper(@JsonProperty(ITEM_ID_COLUMN) Integer id,
                     @JsonProperty(NAME_COLUMN) String name,
                     @JsonProperty(CATEGORY_COLUMN) String category,
                     @JsonProperty(PRICE_COLUMN) int price,
                     @JsonProperty(QUANTITY_COLUMN) int quantity,
                     @JsonProperty(IS_AVAILABLE_IN_STORE_COLUMN) boolean isAvailableInStore,
                     @JsonProperty(PAGES_COLUMN) int pages) {
        super(id, name, category, price, quantity, isAvailableInStore, pages);
    }

    @Override
    public String toString() {
        return "Newspaper{" +
                "id=" + getId() + 
                ", name=" + getName() +
                ", category=" + getCategory() +
                ", price=" + getPrice() +
                ", quantity=" + getQuantity() +
                ", pages=" + getPages() +
                '}';
    }

}
