package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vshmaliukh.webstore.model.items.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ORDER_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "orderProducts")
public class Order extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = ORDER_DATE_COLUMN, nullable = false)
    @JsonFormat(pattern = DD_MM_YYYY_ORDER_DATE_PATTERN_STR)
    private Date dateCreated;

    @Column(name = ORDER_STATUS_COLUMN)
    private String status;

    @Column(name = ORDER_COMMENT_COLUMN)
    private String comment;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "Order_OrderItem",
            joinColumns = @JoinColumn(
                    name = "order_id",
                    referencedColumnName = "order_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "order_item_id",
                    referencedColumnName = "order_item_id")
    )
    private List<OrderItem> orderItemList;

}