package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Order findByUserId(Long userId);

    void deleteOrderByUserId(Long userId);

    Order findOrderByUserId(Long userId);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByUsernameContainingIgnoreCase(String keyword, Pageable pageable);

}
