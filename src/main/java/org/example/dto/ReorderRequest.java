package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.entity.ReorderType;

import java.math.BigDecimal;

public record ReorderRequest(
        @NotBlank String partId,
        @Min(1) int quantity,
        String supplier,
        @DecimalMin("0.0") BigDecimal unitCost,
        @NotNull ReorderType type
) {
}
