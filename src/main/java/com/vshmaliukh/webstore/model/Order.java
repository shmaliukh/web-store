package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
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
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="orderProducts")
public class Order {

    public static final String DD_MM_YYYY_ORDER_DATE_PATTERN_STR = "dd/MM/yyyy";

    @Id
    @Column(name = ORDER_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = DD_MM_YYYY_ORDER_DATE_PATTERN_STR)
    private LocalDate dateCreated;

    private String status;

    @OneToMany(mappedBy = ORDER_TABLE)
//    @Valid
    private List<OrderItem> orderItems = new ArrayList<>();

    @Transient
    public int getTotalOrderPrice() {
        int sum = 0;
        List<OrderItem> orderProducts = getOrderItems();
        for (OrderItem orderItem : orderProducts) {
            sum += orderItem.getTotalPrice();
        }
        return sum;
    }

    @Transient
    public int getNumberOfItems() {
        return this.orderItems.size();
    }

}
