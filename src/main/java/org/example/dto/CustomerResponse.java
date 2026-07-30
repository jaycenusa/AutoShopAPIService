package org.example.dto;

import org.example.entity.Customer;
import org.example.entity.CustomerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CustomerResponse(
        String id,
        String name,
        String email,
        String phone,
        String company,
        int totalOrders,
        LocalDate lastOrder,
        BigDecimal totalSpent,
        CustomerStatus status
) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getCompany(),
                customer.getTotalOrders(),
                customer.getLastOrder(),
                customer.getTotalSpent(),
                customer.getStatus()
        );
    }
}
