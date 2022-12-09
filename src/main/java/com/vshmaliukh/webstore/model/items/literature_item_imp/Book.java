package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshmaliukh.webstore.model.items.LiteratureItem;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = BOOK_TABLE)
public class Book extends LiteratureItem {

    @Column(name = AUTHOR_COLUMN, nullable = false)
    String author;

    @Column(name = DATE_COLUMN, nullable = false)
    Date dateOfIssue;

    @JsonCreator
    public Book(@JsonProperty(ITEM_ID_COLUMN) Integer id,
                @JsonProperty(NAME_COLUMN) String name,
                @JsonProperty(CATEGORY_COLUMN) String category,
                @JsonProperty(PRICE_COLUMN) int price,
                @JsonProperty(QUANTITY_COLUMN) int quantity,
                @JsonProperty(IS_AVAILABLE_IN_STORE_COLUMN) boolean isAvailableInStore,
                @JsonProperty(PAGES_COLUMN) int pages,
                @JsonProperty(AUTHOR_COLUMN) String author,
                @JsonProperty(DATE_COLUMN) Date dateOfIssue) {
        super(id, name, category, price, quantity, isAvailableInStore, pages);
        this.author = author;
        this.dateOfIssue = dateOfIssue;
    }

}