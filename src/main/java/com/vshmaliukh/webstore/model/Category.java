package com.vshmaliukh.webstore.model;

import com.vshmaliukh.webstore.model.items.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Getter
@Setter
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
    private Image image;

    @OneToMany
    private List<Item> itemList = new ArrayList<>();

}
