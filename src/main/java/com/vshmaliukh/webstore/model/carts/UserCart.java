package com.vshmaliukh.webstore.model.carts;

import com.vshmaliukh.webstore.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "User_carts")
public class UserCart extends Cart {

    @OneToOne
    private User user;

}
