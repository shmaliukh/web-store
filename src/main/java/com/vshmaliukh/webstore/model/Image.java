package com.vshmaliukh.webstore.model;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "image_gallery")
public class Image extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_gallery_id", nullable = false)
    private Long id;

    private String name;
    private String type;

    @Lob
    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] imageData;

}
