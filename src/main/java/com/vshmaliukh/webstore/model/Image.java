package com.vshmaliukh.webstore.model;


import com.vshmaliukh.webstore.model.items.Item;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.Arrays;

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
    @ToString.Exclude
    @Column(name = "image", length = Integer.MAX_VALUE)
    private byte[] imageData;

    @ManyToOne
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (getId() != null ? !getId().equals(image.getId()) : image.getId() != null) return false;
        if (getName() != null ? !getName().equals(image.getName()) : image.getName() != null) return false;
        if (getType() != null ? !getType().equals(image.getType()) : image.getType() != null) return false;
        if (!Arrays.equals(getImageData(), image.getImageData())) return false;
        return getItem() != null ? getItem().equals(image.getItem()) : image.getItem() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getImageData());
        result = 31 * result + (getItem() != null ? getItem().hashCode() : 0);
        return result;
    }

}
