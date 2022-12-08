package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.vshmaliukh.webstore.model.items.LiteratureItem;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = COMICS_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN, PUBLISHER_COLUMN})})
public class Comics extends LiteratureItem {

    @Column(name = PUBLISHER_COLUMN, nullable = false)
    String publisher;

    @Override
    public String toString() {
        return "Comics{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed() +
                ", publisher='" + getPublisher() + '\'' +
                '}';
    }
}
