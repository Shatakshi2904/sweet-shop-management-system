package com.sweetshop.backend.sweets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweetshop.backend.sweets.controller.SweetController;
import com.sweetshop.backend.sweets.model.Sweet;
import com.sweetshop.backend.sweets.service.SweetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SweetController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                com.sweetshop.backend.config.SecurityConfig.class,
                com.sweetshop.backend.security.JwtAuthenticationFilter.class
        }))
@AutoConfigureMockMvc(addFilters = false)
class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SweetService sweetService;

    @Test
    void getAllSweets_shouldReturnListOfSweets() throws Exception {
        // given
        List<Sweet> sweets = Arrays.asList(
                new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100),
                new Sweet("2", "Lollipop", "Candy", new BigDecimal("2.99"), 50)
        );
        when(sweetService.getAllSweets()).thenReturn(sweets);

        // when + then
        mockMvc.perform(get("/api/sweets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chocolate"))
                .andExpect(jsonPath("$[0].price").value(5.99))
                .andExpect(jsonPath("$[1].name").value("Lollipop"));
    }

    @Test
    void createSweet_shouldReturnCreatedSweet() throws Exception {
        // given
        Sweet sweet = new Sweet(null, "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        Sweet created = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 100);
        when(sweetService.createSweet(any(Sweet.class))).thenReturn(created);

        // when + then
        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sweet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Chocolate"));

        verify(sweetService, times(1)).createSweet(any(Sweet.class));
    }

    @Test
    void searchSweets_shouldReturnFilteredResults() throws Exception {
        // given
        List<Sweet> sweets = Arrays.asList(
                new Sweet("1", "Chocolate Bar", "Candy", new BigDecimal("5.99"), 100)
        );
        when(sweetService.searchSweets("Chocolate", null, null, null)).thenReturn(sweets);

        // when + then
        mockMvc.perform(get("/api/sweets/search")
                        .param("name", "Chocolate"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Chocolate Bar"));
    }

    @Test
    void purchaseSweet_shouldReturnUpdatedSweet() throws Exception {
        // given
        Sweet updated = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 90);
        when(sweetService.purchaseSweet("1", 10)).thenReturn(updated);

        // when + then
        mockMvc.perform(post("/api/sweets/1/purchase")
                        .param("quantity", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(90));

        verify(sweetService, times(1)).purchaseSweet("1", 10);
    }

    @Test
    void updateSweet_shouldReturnUpdatedSweet() throws Exception {
        // given
        Sweet sweet = new Sweet("1", "Dark Chocolate", "Candy", new BigDecimal("6.99"), 150);
        when(sweetService.updateSweet(eq("1"), any(Sweet.class))).thenReturn(sweet);

        // when + then
        mockMvc.perform(put("/api/sweets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sweet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dark Chocolate"));

        verify(sweetService, times(1)).updateSweet(eq("1"), any(Sweet.class));
    }

    @Test
    void deleteSweet_shouldReturnNoContent() throws Exception {
        // given
        doNothing().when(sweetService).deleteSweet("1");

        // when + then
        mockMvc.perform(delete("/api/sweets/1"))
                .andExpect(status().isNoContent());

        verify(sweetService, times(1)).deleteSweet("1");
    }

    @Test
    void restockSweet_shouldReturnUpdatedSweet() throws Exception {
        // given
        Sweet updated = new Sweet("1", "Chocolate", "Candy", new BigDecimal("5.99"), 150);
        when(sweetService.restockSweet("1", 50)).thenReturn(updated);

        // when + then
        mockMvc.perform(post("/api/sweets/1/restock")
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(150));

        verify(sweetService, times(1)).restockSweet("1", 50);
    }
}