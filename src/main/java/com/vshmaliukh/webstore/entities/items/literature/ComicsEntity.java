package com.vshmaliukh.webstore.entities.items.literature;

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
@Table(name = COMICS_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN, PUBLISHER_COLUMN})})
public class ComicsEntity extends LiteratureItemEntity {

    @Column(name = PUBLISHER_COLUMN, nullable = false)
    String publisher;

    @Override
    public String toString() {
        return "ComicsEntity{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed() +
                ", publisher='" + getPublisher() + '\'' +
                '}';
    }
}
