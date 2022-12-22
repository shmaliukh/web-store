package com.vshmaliukh.webstore.model;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import static com.vshmaliukh.webstore.ConstantsForEntities.*;

@Getter
@Setter
@Entity
@Table(name = ROLE_TABLE)
public class Role extends AuditModel{

    @Id
    @Column(name = ROLE_ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = ROLE_NAME_COLUMN)
    private String name;

}