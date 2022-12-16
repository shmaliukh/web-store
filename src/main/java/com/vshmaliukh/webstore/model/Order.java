package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

import com.vshmaliukh.webstore.model.items.Item;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = ORDER_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderProducts")
public class Order {

    public static final String DD_MM_YYYY_ORDER_DATE_PATTERN_STR = "dd/MM/yyyy";

    @Id
    @Column(name = ORDER_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = USER_ID_COLUMN)
    private Long userId;

    @Column(name = ORDER_DATE_COLUMN)
    @JsonFormat(pattern = DD_MM_YYYY_ORDER_DATE_PATTERN_STR)
    private LocalDate dateCreated;

    @Column(name = ORDER_STATUS_COLUMN)
    private String status;

//    @OneToMany
//    // TODO config tables relationship
//    private List<> itemList = new ArrayList<>();

}
