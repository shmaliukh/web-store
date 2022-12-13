package com.vshmaliukh.webstore.model.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;
import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "Book"),
        @JsonSubTypes.Type(value = Magazine.class, name = "Magazine"),
        @JsonSubTypes.Type(value = Comics.class, name = "Comics"),
        @JsonSubTypes.Type(value = Newspaper.class, name = "Newspaper"),
})
public abstract class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ITEM_ID_COLUMN, nullable = false)
    private Integer id;

    @Column(name = CATEGORY_COLUMN, nullable = false)
    String category;

    @Column(name = NAME_COLUMN, nullable = false)
    String name;

    @Column(name = PRICE_COLUMN, nullable = false)
    int price;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    int quantity;

    @Column(name = IS_AVAILABLE_IN_STORE_COLUMN, nullable = false)
    boolean isAvailableInStore;


    @JsonCreator
    protected Item(@JsonProperty(ITEM_ID_COLUMN) Integer id,
                   @JsonProperty(NAME_COLUMN) String name,
                   @JsonProperty(CATEGORY_COLUMN) String category,
                   @JsonProperty(PRICE_COLUMN) int price,
                   @JsonProperty(QUANTITY_COLUMN) int quantity,
                   @JsonProperty(IS_AVAILABLE_IN_STORE_COLUMN) boolean isAvailableInStore) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.isAvailableInStore = isAvailableInStore;
    }


}
