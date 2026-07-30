package org.example.dto;

import java.math.BigDecimal;

public record CustomerStatsResponse(
        long totalCustomers,
        long activeCustomers,
        long totalOrders,
        BigDecimal totalRevenue
) {
}
