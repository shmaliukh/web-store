package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshmaliukh.webstore.model.items.LiteratureItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;
import static com.vshmaliukh.webstore.ItemUtil.DATE_FORMAT_STR;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = BOOK_TABLE)
public class Book extends LiteratureItem {

    @Column(name = AUTHOR_COLUMN)
    private String author;

    @Column(name = DATE_COLUMN)
    private Date dateOfIssue;

    @JsonCreator
    public Book(@JsonProperty("item_id") Integer id,
                @JsonProperty("category") String category,
                @JsonProperty("name") String name,
                @JsonProperty("currentQuantity") int currentQuantity,
                @JsonProperty("costPrice") int costPrice,
                @JsonProperty("salePrice") int salePrice,
                @JsonProperty("description") String description,
                @JsonProperty("state") String state,
                @JsonProperty("isAvailableInStore") boolean isAvailableInStore,
                @JsonProperty(PAGES_COLUMN) int pages,
                @JsonProperty(AUTHOR_COLUMN) String author,
                @JsonProperty(DATE_COLUMN) Date dateOfIssue) {
        super(id, name, category, currentQuantity, costPrice, salePrice, description, state, isAvailableInStore, pages);
        this.author = author;
        this.dateOfIssue = dateOfIssue;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + getId() +
                ", category='" + getCategory() + '\'' +
                ", name='" + getName() + '\'' +
                ", currentQuantity=" + getCurrentQuantity() +
                ", availableToBuyQuantity=" + getAvailableToBuyQuantity() +
                ", soldOutQuantity=" + getSoldOutQuantity() +
                ", costPrice=" + getCostPrice() +
                ", salePrice=" + getSalePrice() +
                ", description='" + getDescription() + '\'' +
                ", state='" + getState() + '\'' +
                ", isAvailableInStore=" + isAvailableInStore() +
                ", imageList=" + getImageList() +
                ", pages=" + getPages() +
                ", author=" + getAuthor() +
                ", dateOfIssue=" + new SimpleDateFormat(DATE_FORMAT_STR).format(getDateOfIssue()) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        if (!super.equals(o)) return false;

        Book book = (Book) o;

        if (getAuthor() != null ? !getAuthor().equals(book.getAuthor()) : book.getAuthor() != null) return false;
        return getDateOfIssue() != null ? getDateOfIssue().equals(book.getDateOfIssue()) : book.getDateOfIssue() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getAuthor() != null ? getAuthor().hashCode() : 0);
        result = 31 * result + (getDateOfIssue() != null ? getDateOfIssue().hashCode() : 0);
        return result;
    }

}