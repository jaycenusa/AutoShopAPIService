package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "parts")
public class Part {

    @Id
    @Column(length = 64)
    private String id;

    @Column(nullable = false, unique = true, length = 64)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 64)
    private String category;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private int threshold;

    @Column(name = "reorder_qty", nullable = false)
    private int reorderQty;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "markup_pct", nullable = false, precision = 8, scale = 2)
    private BigDecimal markupPct;

    @Column(name = "labour_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal labourCost;

    @Column(nullable = false)
    private String supplier;

    @Column(nullable = false, length = 64)
    private String location;

    @Column(name = "auto_reorder", nullable = false)
    private boolean autoReorder;

    public Part() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public int getReorderQty() {
        return reorderQty;
    }

    public void setReorderQty(int reorderQty) {
        this.reorderQty = reorderQty;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getMarkupPct() {
        return markupPct;
    }

    public void setMarkupPct(BigDecimal markupPct) {
        this.markupPct = markupPct;
    }

    public BigDecimal getLabourCost() {
        return labourCost;
    }

    public void setLabourCost(BigDecimal labourCost) {
        this.labourCost = labourCost;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAutoReorder() {
        return autoReorder;
    }

    public void setAutoReorder(boolean autoReorder) {
        this.autoReorder = autoReorder;
    }
}
