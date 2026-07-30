package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.ReorderRequest;
import org.example.dto.ReorderResponse;
import org.example.dto.ReorderStatusUpdateRequest;
import org.example.entity.ReorderStatus;
import org.example.entity.ReorderType;
import org.example.exception.GlobalExceptionHandler;
import org.example.service.ReorderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReorderController.class)
@Import(GlobalExceptionHandler.class)
class ReorderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ReorderService reorderService;

    @Test
    void createReorderReturnsCreated() throws Exception {
        ReorderRequest request = new ReorderRequest("p6", 20, null, null, ReorderType.manual);
        when(reorderService.create(any(ReorderRequest.class))).thenReturn(
                new ReorderResponse(
                        "r1",
                        "p6",
                        "BRK-ROT-006",
                        "Brake Rotor (Front)",
                        20,
                        "Brembo",
                        new BigDecimal("72.00"),
                        ReorderStatus.pending,
                        ReorderType.manual,
                        LocalDate.of(2026, 7, 13),
                        null
                )
        );

        mockMvc.perform(post("/api/reorders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("r1"))
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    void updateStatusReturnsOk() throws Exception {
        when(reorderService.updateStatus(eq("r1"), any(ReorderStatusUpdateRequest.class))).thenReturn(
                new ReorderResponse(
                        "r1",
                        "p6",
                        "BRK-ROT-006",
                        "Brake Rotor (Front)",
                        20,
                        "Brembo",
                        new BigDecimal("72.00"),
                        ReorderStatus.delivered,
                        ReorderType.manual,
                        LocalDate.of(2026, 7, 13),
                        LocalDate.of(2026, 7, 15)
                )
        );

        mockMvc.perform(patch("/api/reorders/r1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"delivered\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("delivered"));
    }
}
