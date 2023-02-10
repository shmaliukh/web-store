package com.vshmaliukh.webstore.model;

import com.vshmaliukh.webstore.model.items.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Categories")
public class Category extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private boolean isArchived = false;
    private boolean isActivated = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "Category_Image",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "img_id",
                    referencedColumnName = "img_id")
    )
    @ToString.Exclude
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "Category_Item",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "item_id",
                    referencedColumnName = "item_id")
    )
    @ToString.Exclude
    private Set<Item> itemSet;

    public Category(String name, String description) {
        setName(name);
        setDescription(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return isArchived == category.isArchived
                && isActivated == category.isActivated
                && Objects.equals(id, category.id)
                && name.equals(category.name)
                && Objects.equals(description, category.description)
                && Objects.equals(image, category.image)
                && Objects.equals(itemSet, category.itemSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isArchived, isActivated, image, itemSet);
    }

}
