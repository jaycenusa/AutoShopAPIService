package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reorder_entries")
public class ReorderEntry {

    @Id
    @Column(length = 64)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "part_id", nullable = false)
    private Part part;

    @Column(name = "part_sku", nullable = false, length = 64)
    private String partSku;

    @Column(name = "part_name", nullable = false)
    private String partName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String supplier;

    @Column(name = "unit_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitCost;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReorderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private ReorderType type;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "delivered_at")
    private LocalDate deliveredAt;

    public ReorderEntry() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

    public String getPartSku() {
        return partSku;
    }

    public void setPartSku(String partSku) {
        this.partSku = partSku;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(BigDecimal unitCost) {
        this.unitCost = unitCost;
    }

    public ReorderStatus getStatus() {
        return status;
    }

    public void setStatus(ReorderStatus status) {
        this.status = status;
    }

    public ReorderType getType() {
        return type;
    }

    public void setType(ReorderType type) {
        this.type = type;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDeliveredAt() {
        return deliveredAt;
    }

    public void setDeliveredAt(LocalDate deliveredAt) {
        this.deliveredAt = deliveredAt;
    }
}
