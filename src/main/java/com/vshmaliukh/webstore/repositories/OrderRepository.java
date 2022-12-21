package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByUserId(Long userId);

    List<Order> findAllByUserIdAndDateCreated(Long id, Date date);

    Optional<Order> findById(Long id);

    void deleteOrderByUserId(Long userId);

    Order findOrderByUserId(Long userId);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findByItemClassTypeContainingIgnoreCase(String keyword, Pageable pageable);

}
