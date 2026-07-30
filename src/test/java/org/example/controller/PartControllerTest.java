package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.PartRequest;
import org.example.dto.PartResponse;
import org.example.exception.GlobalExceptionHandler;
import org.example.exception.ResourceNotFoundException;
import org.example.service.PartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartController.class)
@Import(GlobalExceptionHandler.class)
class PartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PartService partService;

    @Test
    void listPartsReturnsOk() throws Exception {
        when(partService.findAll(null, null)).thenReturn(List.of(
                new PartResponse(
                        "p1",
                        "ENG-OIL-001",
                        "Oil Filter",
                        "Engine",
                        45,
                        20,
                        100,
                        new BigDecimal("8.99"),
                        new BigDecimal("40"),
                        new BigDecimal("20"),
                        "Bosch",
                        "A1-01",
                        true,
                        "ok"
                )
        ));

        mockMvc.perform(get("/api/parts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("ENG-OIL-001"))
                .andExpect(jsonPath("$[0].status").value("ok"));
    }

    @Test
    void getMissingPartReturnsNotFound() throws Exception {
        when(partService.findById("missing")).thenThrow(new ResourceNotFoundException("Part not found: missing"));

        mockMvc.perform(get("/api/parts/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Part not found: missing"));
    }

    @Test
    void createPartReturnsCreated() throws Exception {
        PartRequest request = new PartRequest(
                "ENG-OIL-001",
                "Oil Filter",
                "Engine",
                45,
                20,
                100,
                new BigDecimal("8.99"),
                new BigDecimal("40"),
                new BigDecimal("20"),
                "Bosch",
                "A1-01",
                true
        );
        when(partService.create(any(PartRequest.class))).thenReturn(
                new PartResponse(
                        "p1",
                        request.sku(),
                        request.name(),
                        request.category(),
                        request.stock(),
                        request.threshold(),
                        request.reorderQty(),
                        request.unitPrice(),
                        request.markupPct(),
                        request.labourCost(),
                        request.supplier(),
                        request.location(),
                        request.autoReorder(),
                        "ok"
                )
        );

        mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("p1"));
    }

    @Test
    void createPartValidatesBody() throws Exception {
        mockMvc.perform(post("/api/parts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }
}
