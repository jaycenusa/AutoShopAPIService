package org.example.dto;

import org.example.entity.ReorderEntry;
import org.example.entity.ReorderStatus;
import org.example.entity.ReorderType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReorderResponse(
        String id,
        String partId,
        String partSku,
        String partName,
        int quantity,
        String supplier,
        BigDecimal unitCost,
        ReorderStatus status,
        ReorderType type,
        LocalDate createdAt,
        LocalDate deliveredAt
) {
    public static ReorderResponse from(ReorderEntry entry) {
        return new ReorderResponse(
                entry.getId(),
                entry.getPart().getId(),
                entry.getPartSku(),
                entry.getPartName(),
                entry.getQuantity(),
                entry.getSupplier(),
                entry.getUnitCost(),
                entry.getStatus(),
                entry.getType(),
                entry.getCreatedAt(),
                entry.getDeliveredAt()
        );
    }
}
