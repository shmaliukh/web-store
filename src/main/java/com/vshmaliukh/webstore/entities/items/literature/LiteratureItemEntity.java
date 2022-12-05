package com.vshmaliukh.webstore.entities.items.literature;

import com.vshmaliukh.webstore.entities.items.ItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@MappedSuperclass
public abstract class LiteratureItemEntity extends ItemEntity {

//    @Column(name = USER_ID_COLUMN, nullable = false)
//    private Integer userId;

    @Column(name = PAGES_COLUMN, nullable = false)
    int pages;

    @Column(name = BORROWED_COLUMN, nullable = false)
    boolean isBorrowed;

}
