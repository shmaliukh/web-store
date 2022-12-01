package com.vshmaliukh.webstore.entities.items;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;


import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name= COMICS_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                USER_ID_COLUMN, NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN, PUBLISHER_COLUMN})})
public class ComicsEntity extends ItemEntity{

    @Column(name = PUBLISHER_COLUMN, nullable = false)
    String publisher;

    @Override
    public String toString() {
        return "ComicsEntity{" +
                "name='" + name + '\'' +
                ", pages=" + pages +
                ", isBorrowed=" + isBorrowed +
                ", publisher='" + publisher + '\'' +
                '}';
    }
}
