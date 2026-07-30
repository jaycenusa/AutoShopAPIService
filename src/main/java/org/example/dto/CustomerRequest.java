package org.example.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.entity.CustomerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String phone,
        @NotBlank String company,
        @Min(0) int totalOrders,
        LocalDate lastOrder,
        @NotNull @DecimalMin("0.0") BigDecimal totalSpent,
        @NotNull CustomerStatus status
) {
}
