package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.vshmaliukh.webstore.model.items.LiteratureItem;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = NEWSPAPER_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN})})
public class Newspaper extends LiteratureItem {

    @Override
    public String toString() {
        return "Newspaper{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed() +
                '}';
    }
}
