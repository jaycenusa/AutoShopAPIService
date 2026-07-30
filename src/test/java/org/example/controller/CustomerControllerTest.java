package org.example.controller;

import org.example.dto.CustomerStatsResponse;
import org.example.exception.GlobalExceptionHandler;
import org.example.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@Import(GlobalExceptionHandler.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @Test
    void listCustomersReturnsOk() throws Exception {
        when(customerService.search(isNull(), isNull())).thenReturn(List.of());

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void statsReturnsAggregates() throws Exception {
        when(customerService.getStats()).thenReturn(
                new CustomerStatsResponse(8, 7, 329, new BigDecimal("96970"))
        );

        mockMvc.perform(get("/api/customers/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCustomers").value(8))
                .andExpect(jsonPath("$.activeCustomers").value(7));
    }
}
