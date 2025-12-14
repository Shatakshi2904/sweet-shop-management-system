package com.sweetshop.backend.sweets;

import com.sweetshop.backend.sweets.exception.InsufficientStockException;
import com.sweetshop.backend.sweets.exception.SweetNotFoundException;
import com.sweetshop.backend.sweets.model.Sweet;
import com.sweetshop.backend.sweets.repository.SweetRepository;
import com.sweetshop.backend.sweets.service.SweetService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SweetServiceTest {

    private final SweetRepository sweetRepository = mock(SweetRepository.class);
    private final SweetService sweetService = new SweetService(sweetRepository);

    @Test
    void createSweet_shouldSaveAndReturnSweet() {
        // given
        Sweet sweet = new Sweet(null, "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        Sweet savedSweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);

        when(sweetRepository.save(any(Sweet.class))).thenReturn(savedSweet);

        // when
        Sweet result = sweetService.createSweet(sweet);

        // then
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Chocolate");
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    void getAllSweets_shouldReturnAllSweets() {
        // given
        List<Sweet> sweets = Arrays.asList(
                new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100),
                new Sweet("2", "Lollipop", "Candy", new BigDecimal("2.99"), 50)
        );
        when(sweetRepository.findAll()).thenReturn(sweets);

        // when
        List<Sweet> result = sweetService.getAllSweets();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Chocolate");
    }

    @Test
    void searchSweets_byName_shouldReturnMatchingSweets() {
        // given
        List<Sweet> sweets = Arrays.asList(
                new Sweet("1", "Chocolate Bar", "Candy", new BigDecimal("5.99"), 100)
        );
        when(sweetRepository.findByNameContainingIgnoreCase("Chocolate")).thenReturn(sweets);

        // when
        List<Sweet> result = sweetService.searchSweets("Chocolate", null, null, null);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).contains("Chocolate");
    }

    @Test
    void purchaseSweet_withSufficientStock_shouldDecreaseQuantity() {
        // given
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweet);

        // when
        Sweet result = sweetService.purchaseSweet("1", 10);

        // then
        assertThat(result.getQuantity()).isEqualTo(90);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    void purchaseSweet_withInsufficientStock_shouldThrowException() {
        // given
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 5);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(sweet));

        // then
        assertThatThrownBy(() -> sweetService.purchaseSweet("1", 10))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void purchaseSweet_withNonExistentSweet_shouldThrowException() {
        // given
        when(sweetRepository.findById("999")).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> sweetService.purchaseSweet("999", 1))
                .isInstanceOf(SweetNotFoundException.class);
    }

    @Test
    void purchaseSweet_withInvalidQuantity_shouldThrowException() {
        // given
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(sweet));

        // then
        assertThatThrownBy(() -> sweetService.purchaseSweet("1", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be positive");
    }

    @Test
    void restockSweet_shouldIncreaseQuantity() {
        // given
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweet);

        // when
        Sweet result = sweetService.restockSweet("1", 50);

        // then
        assertThat(result.getQuantity()).isEqualTo(150);
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    void restockSweet_withNullQuantity_shouldHandleGracefully() {
        // given
        Sweet sweet = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), null);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(sweet));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(sweet);

        // when
        Sweet result = sweetService.restockSweet("1", 50);

        // then
        assertThat(result.getQuantity()).isEqualTo(50);
    }

    @Test
    void updateSweet_shouldUpdateExistingSweet() {
        // given
        Sweet existing = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        Sweet updated = new Sweet("1", "Dark Chocolate", "Candy", new BigDecimal("6.99"), 150);
        when(sweetRepository.findById("1")).thenReturn(Optional.of(existing));
        when(sweetRepository.save(any(Sweet.class))).thenReturn(existing);

        // when
        sweetService.updateSweet("1", updated);

        // then
        verify(sweetRepository, times(1)).save(any(Sweet.class));
    }

    @Test
    void updateSweet_withNonExistentId_shouldThrowException() {
        // given
        Sweet updated = new Sweet("999", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        when(sweetRepository.findById("999")).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> sweetService.updateSweet("999", updated))
                .isInstanceOf(SweetNotFoundException.class);
    }

    @Test
    void deleteSweet_withValidId_shouldDelete() {
        // given
        when(sweetRepository.existsById("1")).thenReturn(true);

        // when
        sweetService.deleteSweet("1");

        // then
        verify(sweetRepository, times(1)).deleteById("1");
    }

    @Test
    void deleteSweet_withInvalidId_shouldThrowException() {
        // given
        when(sweetRepository.existsById("999")).thenReturn(false);

        // then
        assertThatThrownBy(() -> sweetService.deleteSweet("999"))
                .isInstanceOf(SweetNotFoundException.class);
    }
}