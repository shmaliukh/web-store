package com.vshmaliukh.webstore.model.carts;

import com.vshmaliukh.webstore.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static com.vshmaliukh.webstore.ConstantsForEntities.USER_COLUMN;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "User_carts")
public class UserCart extends Cart {

    @ManyToOne
    private User user;

}
