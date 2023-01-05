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

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = COMICS_TABLE)
public class Comics extends LiteratureItem {

    @Column(name = PUBLISHER_COLUMN)
    private String publisher;

    @JsonCreator
    public Comics(@JsonProperty(ITEM_ID_COLUMN) Integer id,
                  @JsonProperty(NAME_COLUMN) String name,
                  @JsonProperty(CATEGORY_COLUMN) String category,
                  @JsonProperty(PRICE_COLUMN) int price,
                  @JsonProperty(QUANTITY_COLUMN) int quantity,
                  @JsonProperty(IS_AVAILABLE_IN_STORE_COLUMN) boolean isAvailableInStore,
                  @JsonProperty(PAGES_COLUMN) int pages,
                  @JsonProperty(PUBLISHER_COLUMN) String publisher) {
        super(id, name, category, price, quantity, isAvailableInStore, pages);
        this.publisher = publisher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comics)) return false;
        if (!super.equals(o)) return false;

        Comics comics = (Comics) o;

        return getPublisher() != null ? getPublisher().equals(comics.getPublisher()) : comics.getPublisher() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getPublisher() != null ? getPublisher().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comics{" +
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
                ", publisher=" + getPublisher() +
                '}';
    }

}
