package com.vshmaliukh.webstore.model.carts;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Unauthorized_user_carts")
public class UnauthorizedUserCart extends Cart{

    @OneToOne
    private UnauthorizedUser unauthorizedUser;

}
