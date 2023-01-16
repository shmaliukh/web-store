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
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Categories")
public class Category extends AuditModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    private String name;
    @Column(columnDefinition="LONGTEXT")
    private String description;

    private boolean isDeleted = false;
    private boolean isActivated = true;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "Image_Category",
            joinColumns = @JoinColumn(
                    name = "category_id",
                    referencedColumnName = "category_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "img_id",
                    referencedColumnName = "img_id")
    )
    @ToString.Exclude
    private Image image;

    @OneToMany(cascade = CascadeType.PERSIST)
    @ToString.Exclude
    private Set<Item> itemSet;

}
