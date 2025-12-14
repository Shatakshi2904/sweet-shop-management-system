package com.sweetshop.backend.sweets.service;

import com.sweetshop.backend.sweets.exception.InsufficientStockException;
import com.sweetshop.backend.sweets.exception.SweetNotFoundException;
import com.sweetshop.backend.sweets.model.Sweet;
import com.sweetshop.backend.sweets.repository.SweetRepository;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;

@Service
public class SweetService {

    private final SweetRepository sweetRepository;

    public SweetService(SweetRepository sweetRepository) {
        this.sweetRepository = sweetRepository;
    }

    public Sweet createSweet(Sweet sweet) {
        sweet.setId(null);
        return sweetRepository.save(sweet);
    }

    public List<Sweet> getAllSweets() {
        return sweetRepository.findAll();
    }

    public List<Sweet> searchSweets(String name, String category, BigDecimal minPrice, BigDecimal maxPrice) {
        // If only name is provided, use repository method
        if (name != null && !name.isBlank() && 
            (category == null || category.isBlank()) && 
            minPrice == null && maxPrice == null) {
            return sweetRepository.findByNameContainingIgnoreCase(name);
        }
        
        // If only category is provided, use repository method
        if (category != null && !category.isBlank() && 
            (name == null || name.isBlank()) && 
            minPrice == null && maxPrice == null) {
            return sweetRepository.findByCategoryIgnoreCase(category);
        }
        
        // If only price range is provided, use repository method
        if (minPrice != null && maxPrice != null && 
            (name == null || name.isBlank()) && 
            (category == null || category.isBlank())) {
            return sweetRepository.findByPriceBetween(minPrice, maxPrice);
        }
        
        // For complex queries, filter in memory
        List<Sweet> results = sweetRepository.findAll();
        List<Sweet> filtered = new java.util.ArrayList<>(results);
        
        // Apply name filter
        if (name != null && !name.isBlank()) {
            filtered.removeIf(sweet -> 
                sweet.getName() == null || 
                !sweet.getName().toLowerCase().contains(name.toLowerCase())
            );
        }
        
        // Apply category filter
        if (category != null && !category.isBlank()) {
            filtered.removeIf(sweet -> 
                sweet.getCategory() == null || 
                !sweet.getCategory().equalsIgnoreCase(category)
            );
        }
        
        // Apply price range filter
        if (minPrice != null) {
            filtered.removeIf(sweet -> 
                sweet.getPrice() == null || 
                sweet.getPrice().compareTo(minPrice) < 0
            );
        }
        
        if (maxPrice != null) {
            filtered.removeIf(sweet -> 
                sweet.getPrice() == null || 
                sweet.getPrice().compareTo(maxPrice) > 0
            );
        }
        
        return filtered;
    }

    public Sweet updateSweet(String id, Sweet updated) {
        Sweet existing = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found: " + id));
        existing.setName(updated.getName());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());
        return sweetRepository.save(existing);
    }

    public void deleteSweet(String id) {
        if (!sweetRepository.existsById(id)) {
            throw new SweetNotFoundException("Sweet not found: " + id);
        }
        sweetRepository.deleteById(id);
    }

    public Sweet purchaseSweet(String id, int quantityToPurchase) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found: " + id));

        if (quantityToPurchase <= 0) {
            throw new IllegalArgumentException("Quantity to purchase must be positive");
        }

        if (sweet.getQuantity() == null || sweet.getQuantity() < quantityToPurchase) {
            throw new InsufficientStockException("Insufficient stock for sweet: " + id);
        }

        sweet.setQuantity(sweet.getQuantity() - quantityToPurchase);
        return sweetRepository.save(sweet);
    }

    public Sweet restockSweet(String id, int quantityToAdd) {
        Sweet sweet = sweetRepository.findById(id)
                .orElseThrow(() -> new SweetNotFoundException("Sweet not found: " + id));

        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity to restock must be positive");
        }

        int newQuantity = (sweet.getQuantity() == null ? 0 : sweet.getQuantity()) + quantityToAdd;
        sweet.setQuantity(newQuantity);
        return sweetRepository.save(sweet);
    }
}
