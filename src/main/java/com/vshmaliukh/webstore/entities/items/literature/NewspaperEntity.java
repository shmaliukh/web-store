package com.vshmaliukh.webstore.entities.items.literature;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = NEWSPAPER_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN})})
public class NewspaperEntity extends LiteratureItemEntity {

    @Override
    public String toString() {
        return "NewspaperEntity{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed() +
                '}';
    }
}
