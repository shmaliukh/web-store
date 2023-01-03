package com.vshmaliukh.webstore.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.vshmaliukh.webstore.ConstantsForEntities.UNAUTHORIZED_USER_TABLE;

@Getter
@Setter
@Entity
@Table(name = UNAUTHORIZED_USER_TABLE)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "unauthorizedUsers")
public class UnauthorizedUser extends AuditModel {

    @Id
    private Long id;

}
