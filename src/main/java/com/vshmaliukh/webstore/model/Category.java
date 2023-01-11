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
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", nullable = false)
    private Integer id;

    private String name;
    private String description;

    @OneToOne
//    @JoinColumn(name = "img_id", referencedColumnName = "category_id")
    private Image image;

    @OneToMany()
    @ToString.Exclude
    private Set<Item> itemSet;

}
