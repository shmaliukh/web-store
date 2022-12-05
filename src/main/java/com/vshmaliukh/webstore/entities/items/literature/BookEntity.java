package com.vshmaliukh.webstore.entities.items.literature;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
public class BookEntity extends LiteratureItemEntity {

    @Column(name = AUTHOR_COLUMN, nullable = false)
    String author;

    @Column(name = DATE_COLUMN, nullable = false)
    Date dateOfIssue;

    @Override
    public String toString() {
        return "BookEntity{" +
                "name='" + getName() + '\'' +
                ", pages=" + getPages() +
                ", isBorrowed=" + isBorrowed +
                ", author='" + getAuthor() + '\'' +
                ", dateOfIssue=" + getDateOfIssue() +
                '}';
    }
}