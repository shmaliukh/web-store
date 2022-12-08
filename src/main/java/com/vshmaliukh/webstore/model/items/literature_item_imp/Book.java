package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.vshmaliukh.webstore.model.items.LiteratureItem;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = BOOK_TABLE,
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                NAME_COLUMN, PAGES_COLUMN, BORROWED_COLUMN, AUTHOR_COLUMN, DATE_COLUMN})})
public class Book extends LiteratureItem {

    @Column(name = AUTHOR_COLUMN, nullable = false)
    String author;

    @Column(name = DATE_COLUMN, nullable = false)
    Date dateOfIssue;

    @Override
    public String toString() {
        return "Book{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed() +
                ", author='" + getAuthor() + '\'' +
                ", dateOfIssue=" + getDateOfIssue() +
                '}';
    }
}