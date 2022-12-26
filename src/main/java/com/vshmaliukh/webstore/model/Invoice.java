package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = INVOICE_TABLE)
public class Invoice extends AuditModel {

    @Id
    @Column(name = INVOICE_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = USER_ID_COLUMN, nullable = false)
    private Long userId;

    @Column(name = INVOICE_DATE_COLUMN, nullable = false)
    @JsonFormat(pattern = DD_MM_YYYY_ORDER_DATE_PATTERN_STR)
    private Date date;

    @Column(name = INVOICE_STATUS_COLUMN)
    private String status;

    @Column(name = INVOICE_TOTAL_AMOUNT_COLUMN)
    private Integer totalAmount;

}
