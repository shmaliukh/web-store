package com.vshmaliukh.webstore.model.items.literature_item_imp;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.vshmaliukh.webstore.model.items.LiteratureItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = MAGAZINE_TABLE)
public class Magazine extends LiteratureItem {

    @JsonCreator
    public Magazine(@JsonProperty("itemId") Integer id,
                    @JsonProperty("category") String category,
                    @JsonProperty("name") String name,
                    @JsonProperty("currentQuantity") int currentQuantity,
                    @JsonProperty("availableToBuyQuantity") int availableToBuyQuantity,
                    @JsonProperty("costPrice") int costPrice,
                    @JsonProperty("salePrice") int salePrice,
                    @JsonProperty("description") String description,
                    @JsonProperty("status") String status,
                    @JsonProperty("isAvailableInStore") boolean isAvailableInStore,
                    @JsonProperty("soldOutQuantity") int soldOutQuantity,
                    @JsonProperty("pages") int pages) {
        super(id, name, category, currentQuantity, availableToBuyQuantity, costPrice, salePrice, description, status, isAvailableInStore, soldOutQuantity, pages);
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "id=" + getId() +
                ", category='" + getCategory() + '\'' +
                ", name='" + getName() + '\'' +
                ", currentQuantity=" + getCurrentQuantity() +
                ", availableToBuyQuantity=" + getAvailableToBuyQuantity() +
                ", soldOutQuantity=" + getSoldOutQuantity() +
                ", costPrice=" + getCostPrice() +
                ", salePrice=" + getSalePrice() +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", isAvailableInStore=" + isAvailableInStore() +
                ", imageList=" + getImageList() +
                ", pages=" + getPages() +
                '}';
    }

}
