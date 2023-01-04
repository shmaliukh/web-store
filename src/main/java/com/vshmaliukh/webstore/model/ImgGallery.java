package com.vshmaliukh.webstore.model;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@Entity
@Table(name = "image_gallery")
public class ImgGallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price",nullable = false, precision = 10, scale = 2)
    private double price;

    @Lob
    @Column(name = "Image", length = Integer.MAX_VALUE)
    private byte[] image;

}
