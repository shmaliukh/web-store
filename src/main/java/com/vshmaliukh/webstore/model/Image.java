package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

import java.util.Arrays;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;


@Slf4j
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({@JsonSubTypes.Type(value = ItemImage.class, name = "itemImage")})
public class Image extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    private String name;
    private String type;

    @Lob
    @ToString.Exclude
    @Column(name = "image", length = Integer.MAX_VALUE)
    // FIXME refactor image data storage (save images files on local machine instead of database)
    private byte[] imageData;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (getId() != null ? !getId().equals(image.getId()) : image.getId() != null) return false;
        if (getName() != null ? !getName().equals(image.getName()) : image.getName() != null) return false;
        if (getType() != null ? !getType().equals(image.getType()) : image.getType() != null) return false;
        return Arrays.equals(getImageData(), image.getImageData());
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getImageData());
        return result;
    }

}
