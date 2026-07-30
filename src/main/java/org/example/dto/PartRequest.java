package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PartRequest(
        @NotBlank String sku,
        @NotBlank String name,
        @NotBlank String category,
        @Min(0) int stock,
        @Min(0) int threshold,
        @Min(1) int reorderQty,
        @NotNull @DecimalMin("0.0") BigDecimal unitPrice,
        @NotNull @DecimalMin("0.0") BigDecimal markupPct,
        @NotNull @DecimalMin("0.0") BigDecimal labourCost,
        @NotBlank String supplier,
        @NotBlank String location,
        boolean autoReorder
) {
}
