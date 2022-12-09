package com.vshmaliukh.webstore.model.items;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class LiteratureItem extends Item {

    @Column(name = PAGES_COLUMN, nullable = false)
    int pages;

    protected LiteratureItem(Integer id, String category, String name, int price, int quantity, boolean isAvailableInStore, int pages) {
        super(id, category, name, price, quantity, isAvailableInStore);
        this.pages = pages;
    }

}
