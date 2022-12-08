package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

}
