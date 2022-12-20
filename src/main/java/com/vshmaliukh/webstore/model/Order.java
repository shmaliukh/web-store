package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ORDER_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderProducts")
public class Order {

    public static final String DD_MM_YYYY_ORDER_DATE_PATTERN_STR = "dd/MM/yyyy";

    @Id
    @Column(name = ORDER_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = USER_ID_COLUMN, nullable = false)
    private Long userId;

    @Column(name = ORDER_DATE_COLUMN, nullable = false)
    @JsonFormat(pattern = DD_MM_YYYY_ORDER_DATE_PATTERN_STR)
    private Date dateCreated;

    @Column(name = ORDER_STATUS_COLUMN)
    private String status;

    @Column(name = ORDER_COMMENT_COLUMN)
    private String comment;

    @Column(name = ITEM_ID_COLUMN, nullable = false)
    private Integer itemId;

    @Column(name = ITEM_CLASS_TYPE_COLUMN, nullable = false)
    private String itemClassType;

    @Column(name = QUANTITY_COLUMN, nullable = false)
    private Integer quantity;

//    @OneToMany
//    // TODO config tables relationship
//    private List<> itemList = new ArrayList<>();

}
