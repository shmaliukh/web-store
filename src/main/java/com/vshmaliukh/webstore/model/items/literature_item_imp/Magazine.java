package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@Table(name = MAGAZINE_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN
        })})
public class Magazine extends LiteratureItem {

    @JsonCreator
    public Magazine(
            @JsonProperty(ITEM_ID_COLUMN) Integer id,
            @JsonProperty(NAME_COLUMN) String name,
            @JsonProperty(CATEGORY_COLUMN) String category,
            @JsonProperty(PRICE_COLUMN) int price,
            @JsonProperty(QUANTITY_COLUMN) int quantity,
            @JsonProperty(IS_AVAILABLE_IN_STORE_COLUMN) boolean isAvailableInStore,
            @JsonProperty(PAGES_COLUMN) int pages
    ) {
        super(id, name, category, price, quantity, isAvailableInStore, pages);
    }

}
