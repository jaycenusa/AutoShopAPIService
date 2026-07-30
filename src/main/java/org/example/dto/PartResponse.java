package org.example.dto;

import org.example.entity.Part;

import java.math.BigDecimal;

public record PartResponse(
        String id,
        String sku,
        String name,
        String category,
        int stock,
        int threshold,
        int reorderQty,
        BigDecimal unitPrice,
        BigDecimal markupPct,
        BigDecimal labourCost,
        String supplier,
        String location,
        boolean autoReorder,
        String status
) {
    public static PartResponse from(Part part) {
        return new PartResponse(
                part.getId(),
                part.getSku(),
                part.getName(),
                part.getCategory(),
                part.getStock(),
                part.getThreshold(),
                part.getReorderQty(),
                part.getUnitPrice(),
                part.getMarkupPct(),
                part.getLabourCost(),
                part.getSupplier(),
                part.getLocation(),
                part.isAutoReorder(),
                deriveStatus(part.getStock(), part.getThreshold())
        );
    }

    static String deriveStatus(int stock, int threshold) {
        if (stock <= 0) {
            return "out";
        }
        if (threshold > 0 && stock <= threshold / 2) {
            return "critical";
        }
        if (stock <= threshold) {
            return "low";
        }
        return "ok";
    }
}
