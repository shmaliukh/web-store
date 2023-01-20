package com.vshmaliukh.webstore.model;

import com.vshmaliukh.webstore.model.items.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
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
    @Column(name = "category_id", nullable = false)
    private Integer id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "LONGTEXT")
    private String description;

    private boolean isDeleted = false;
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

}
