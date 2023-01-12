package com.vshmaliukh.webstore.model.carts;

import com.vshmaliukh.webstore.model.UnauthorizedUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import static com.vshmaliukh.webstore.ConstantsForEntities.UNAUTHORIZED_USER_COLUMN;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "Unauthorized_user_carts")
public class UnauthorizedUserCart extends Cart{

    @ManyToOne
    private UnauthorizedUser unauthorizedUser;

}
