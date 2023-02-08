package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Order;
import com.vshmaliukh.webstore.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserId(Long userId);

    Optional<Order> findById(Long id);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByStatusContainingIgnoreCase(String keyword, Pageable pageable);

    List<Order> findAllByUser(User user);

}
