package com.vshmaliukh.webstore.model.items;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.vshmaliukh.webstore.model.AuditModel;
import com.vshmaliukh.webstore.model.ItemImage;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Book;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Comics;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Magazine;
import com.vshmaliukh.webstore.model.items.literature_item_imp.Newspaper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "item")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Book.class, name = "book"),
        @JsonSubTypes.Type(value = Magazine.class, name = "magazine"),
        @JsonSubTypes.Type(value = Comics.class, name = "comics"),
        @JsonSubTypes.Type(value = Newspaper.class, name = "newspaper"),
})
public abstract class Item extends AuditModel {

    public static final int DEFAULT_PRICE = 0;
    public static final int DEFAULT_QUANTITY = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", nullable = false)
    private Integer id;

    @Column(nullable = false)
    //TODO implement category entity
    private String category;

    @Column(nullable = false, unique = true)
    private String name;

    private int currentQuantity;
    private int availableToBuyQuantity;
    private int soldOutQuantity;

    private int costPrice;
    private int salePrice;

    private String description;
    private String state;

    private boolean isAvailableInStore;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemImage> imageList = new ArrayList<>();

    @JsonCreator
    protected Item(@JsonProperty("item_id") Integer id,
                   @JsonProperty("category") String category,
                   @JsonProperty("name") String name,
                   @JsonProperty("currentQuantity") int currentQuantity,
                   @JsonProperty("costPrice") int costPrice,
                   @JsonProperty("salePrice") int salePrice,
                   @JsonProperty("description") String description,
                   @JsonProperty("state") String state,
                   @JsonProperty("isAvailableInStore") boolean isAvailableInStore) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.currentQuantity = currentQuantity;
        this.availableToBuyQuantity = currentQuantity;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.description = description;
        this.state = state;
        this.isAvailableInStore = isAvailableInStore;
        soldOutQuantity = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (getCurrentQuantity() != item.getCurrentQuantity()) return false;
        if (getAvailableToBuyQuantity() != item.getAvailableToBuyQuantity()) return false;
        if (getCostPrice() != item.getCostPrice()) return false;
        if (getSalePrice() != item.getSalePrice()) return false;
        if (isAvailableInStore() != item.isAvailableInStore()) return false;
        if (getId() != null ? !getId().equals(item.getId()) : item.getId() != null) return false;
        if (getCategory() != null ? !getCategory().equals(item.getCategory()) : item.getCategory() != null) return false;
        return getName() != null ? getName().equals(item.getName()) : item.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + getCurrentQuantity();
        result = 31 * result + getAvailableToBuyQuantity();
        result = 31 * result + getCostPrice();
        result = 31 * result + getSalePrice();
        result = 31 * result + (isAvailableInStore() ? 1 : 0);
        return result;
    }

    public void setCostPrice(int costPrice) {
        if (costPrice >= 0) {
            this.costPrice = costPrice;
        } else {
            this.costPrice = DEFAULT_PRICE;
            log.warn("item id: '{}' // invalid costPrice value to set: '{}' // set up default costPrice value: '{}'",
                    getId(), costPrice, DEFAULT_PRICE);
        }
    }

    public void setSalePrice(int salePrice) {
        if (salePrice >= 0) {
            this.salePrice = salePrice;
        } else {
            this.salePrice = DEFAULT_PRICE;
            log.warn("item id: '{}' // invalid salePrice value to set: '{}' // set up default salePrice value: '{}'",
                    getId(), salePrice, DEFAULT_PRICE);
        }
    }

    public void setSoldOutQuantity(int soldQuantity) {
        if (Item.this.soldOutQuantity >= 0) {
            this.soldOutQuantity = soldQuantity;
        } else {
            this.soldOutQuantity = DEFAULT_PRICE;
            log.warn("item id: '{}' // invalid soldQuantity value to set: '{}' // set up default soldQuantity value: '{}'",
                    getId(), Item.this.soldOutQuantity, DEFAULT_QUANTITY);
        }
    }

    public String getTypeStr() {
        return getClass().getSimpleName().toLowerCase();
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
                ", currentQuantity=" + currentQuantity +
                ", availableToBuyQuantity=" + availableToBuyQuantity +
                ", soldOutQuantity=" + soldOutQuantity +
                ", costPrice=" + costPrice +
                ", salePrice=" + salePrice +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                ", isAvailableInStore=" + isAvailableInStore +
                ", imageList=" + imageList +
                '}';
    }

}
