package com.vshmaliukh.webstore.repositories;

import com.vshmaliukh.webstore.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
