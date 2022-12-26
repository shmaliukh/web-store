package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserId(Long userId);

    Optional<Order> findById(Long id);

    void deleteOrderByUserId(Long userId);
    
    Page<Order> findAll(Pageable pageable);

    // TODO implement pagination
    Page<Order> findByStatusIgnoreCase(String keyword, Pageable pageable);

}
