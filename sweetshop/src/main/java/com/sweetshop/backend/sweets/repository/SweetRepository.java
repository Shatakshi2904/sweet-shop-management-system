package com.sweetshop.backend.sweets.repository;

import com.sweetshop.backend.sweets.model.Sweet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.math.BigDecimal;
import java.util.List;

public interface SweetRepository extends MongoRepository<Sweet, String> {

    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategoryIgnoreCase(String category);

    List<Sweet> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
}

