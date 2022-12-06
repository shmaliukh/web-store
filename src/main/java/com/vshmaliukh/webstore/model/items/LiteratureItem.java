package com.vshmaliukh.webstore.model.items;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@MappedSuperclass
public abstract class LiteratureItem extends Item {

//    @Column(name = USER_ID_COLUMN, nullable = false)
//    private Integer userId;

    @Column(name = PAGES_COLUMN, nullable = false)
    int pages;

    @Column(name = BORROWED_COLUMN, nullable = false)
    boolean isBorrowed;

}
