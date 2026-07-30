package org.example.config;

import org.example.entity.Customer;
import org.example.entity.CustomerStatus;
import org.example.entity.Part;
import org.example.entity.ReorderEntry;
import org.example.entity.ReorderStatus;
import org.example.entity.ReorderType;
import org.example.repository.CustomerRepository;
import org.example.repository.PartRepository;
import org.example.repository.ReorderEntryRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class DataSeeder implements ApplicationRunner {

    private final PartRepository partRepository;
    private final CustomerRepository customerRepository;
    private final ReorderEntryRepository reorderEntryRepository;

    public DataSeeder(
            PartRepository partRepository,
            CustomerRepository customerRepository,
            ReorderEntryRepository reorderEntryRepository
    ) {
        this.partRepository = partRepository;
        this.customerRepository = customerRepository;
        this.reorderEntryRepository = reorderEntryRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (partRepository.count() > 0) {
            return;
        }
        seedParts();
        seedCustomers();
        seedReorders();
    }

    private void seedParts() {
        partRepository.save(part("p1", "ENG-OIL-001", "Oil Filter", "Engine", 45, 20, 100, "8.99", "40", "20", "Bosch", "A1-01", true));
        partRepository.save(part("p2", "ENG-AIR-002", "Air Filter", "Engine", 12, 15, 60, "14.50", "40", "15", "K&N", "A1-02", true));
        partRepository.save(part("p3", "ENG-SPK-003", "Spark Plug Set (4)", "Engine", 8, 20, 80, "22.00", "35", "60", "NGK", "A1-03", false));
        partRepository.save(part("p4", "ENG-TIM-004", "Timing Belt", "Engine", 3, 10, 25, "65.00", "30", "180", "Gates", "A2-01", true));
        partRepository.save(part("p5", "BRK-PAD-005", "Brake Pads (Front)", "Brakes", 28, 15, 50, "38.00", "40", "80", "Brembo", "B1-01", true));
        partRepository.save(part("p6", "BRK-ROT-006", "Brake Rotor (Front)", "Brakes", 0, 8, 20, "72.00", "35", "100", "Brembo", "B1-02", false));
        partRepository.save(part("p7", "BRK-FLD-007", "Brake Fluid DOT4", "Brakes", 6, 10, 30, "9.50", "50", "40", "Prestone", "B2-01", true));
        partRepository.save(part("p8", "SUS-SHK-008", "Shock Absorber (F)", "Suspension", 14, 8, 20, "120.00", "30", "120", "Monroe", "C1-01", false));
        partRepository.save(part("p9", "SUS-ARM-009", "Control Arm (F-L)", "Suspension", 5, 6, 12, "95.00", "25", "140", "Moog", "C1-02", false));
        partRepository.save(part("p10", "ELC-ALT-010", "Alternator 90A", "Electrical", 4, 5, 10, "185.00", "25", "100", "Denso", "D1-01", false));
        partRepository.save(part("p11", "ELC-BAT-011", "Car Battery 60Ah", "Electrical", 18, 10, 20, "95.00", "30", "30", "Varta", "D1-02", true));
        partRepository.save(part("p12", "ELC-STR-012", "Starter Motor", "Electrical", 2, 4, 8, "145.00", "25", "90", "Bosch", "D2-01", false));
        partRepository.save(part("p13", "TRN-FLD-013", "Trans. Fluid ATF", "Transmission", 32, 15, 60, "12.00", "45", "60", "Castrol", "E1-01", true));
        partRepository.save(part("p14", "TRN-CLT-014", "Clutch Kit", "Transmission", 7, 5, 10, "220.00", "25", "400", "LUK", "E1-02", false));
        partRepository.save(part("p15", "ENG-WTP-015", "Water Pump", "Engine", 9, 6, 15, "68.00", "30", "150", "Gates", "A3-01", false));
        partRepository.save(part("p16", "ENG-THS-016", "Thermostat", "Engine", 22, 12, 30, "18.00", "35", "80", "Wahler", "A3-02", true));
        partRepository.save(part("p17", "BRK-CAL-017", "Brake Caliper (F-L)", "Brakes", 0, 4, 8, "88.00", "30", "120", "TRW", "B3-01", false));
        partRepository.save(part("p18", "SUS-BLJ-018", "Ball Joint (Front)", "Suspension", 11, 8, 16, "42.00", "35", "90", "Moog", "C2-01", false));
        partRepository.save(part("p19", "ELC-FUS-019", "Fuse Box Kit", "Electrical", 25, 10, 20, "28.00", "35", "60", "Hella", "D3-01", true));
        partRepository.save(part("p20", "ENG-GKT-020", "Head Gasket Set", "Engine", 6, 5, 10, "110.00", "25", "600", "Elring", "A4-01", false));
    }

    private void seedCustomers() {
        customerRepository.save(customer("c1", "Marcus Webb", "m.webb@autoshop.com", "+1 555-0101", "Webb Auto Repair", 47, "2026-07-10", "12450", CustomerStatus.active));
        customerRepository.save(customer("c2", "Sandra Kowalski", "s.kowalski@kservice.net", "+1 555-0102", "K & Sons Service", 23, "2026-07-08", "5870", CustomerStatus.active));
        customerRepository.save(customer("c3", "James Oduya", "james@premierparts.co", "+1 555-0103", "Premier parts Ltd.", 88, "2026-07-13", "31200", CustomerStatus.active));
        customerRepository.save(customer("c4", "Priya Natarajan", "p.nat@autofix.io", "+1 555-0104", "AutoFix Solutions", 15, "2026-06-22", "3400", CustomerStatus.active));
        customerRepository.save(customer("c5", "Tom Gunderson", "tg@roadready.com", "+1 555-0105", "Road Ready Garage", 62, "2026-07-11", "18750", CustomerStatus.active));
        customerRepository.save(customer("c6", "Leila Ahmadi", "lahmadi@quicklube.net", "+1 555-0106", "Quick Lube Express", 9, "2026-05-15", "1200", CustomerStatus.inactive));
        customerRepository.save(customer("c7", "Cory Whitfield", "c.whitfield@cwmotors.com", "+1 555-0107", "CW Motors", 34, "2026-07-01", "9800", CustomerStatus.active));
        customerRepository.save(customer("c8", "Nneka Obi", "n.obi@centralgarage.net", "+1 555-0108", "Central Garage", 51, "2026-07-12", "14300", CustomerStatus.active));
    }

    private void seedReorders() {
        reorderEntryRepository.save(reorder("r1", "p6", 20, "Brembo", "72.00", ReorderStatus.pending, ReorderType.manual, "2026-07-13", null));
        reorderEntryRepository.save(reorder("r2", "p4", 25, "Gates", "65.00", ReorderStatus.ordered, ReorderType.auto, "2026-07-12", null));
        reorderEntryRepository.save(reorder("r3", "p3", 80, "NGK", "22.00", ReorderStatus.pending, ReorderType.manual, "2026-07-14", null));
        reorderEntryRepository.save(reorder("r4", "p2", 60, "K&N", "14.50", ReorderStatus.delivered, ReorderType.auto, "2026-07-05", "2026-07-10"));
        reorderEntryRepository.save(reorder("r5", "p12", 8, "Bosch", "145.00", ReorderStatus.pending, ReorderType.manual, "2026-07-14", null));
        reorderEntryRepository.save(reorder("r6", "p10", 10, "Denso", "185.00", ReorderStatus.ordered, ReorderType.manual, "2026-07-11", null));
        reorderEntryRepository.save(reorder("r7", "p17", 8, "TRW", "88.00", ReorderStatus.delivered, ReorderType.manual, "2026-07-02", "2026-07-08"));
    }

    private static Part part(
            String id,
            String sku,
            String name,
            String category,
            int stock,
            int threshold,
            int reorderQty,
            String unitPrice,
            String markupPct,
            String labourCost,
            String supplier,
            String location,
            boolean autoReorder
    ) {
        Part part = new Part();
        part.setId(id);
        part.setSku(sku);
        part.setName(name);
        part.setCategory(category);
        part.setStock(stock);
        part.setThreshold(threshold);
        part.setReorderQty(reorderQty);
        part.setUnitPrice(new BigDecimal(unitPrice));
        part.setMarkupPct(new BigDecimal(markupPct));
        part.setLabourCost(new BigDecimal(labourCost));
        part.setSupplier(supplier);
        part.setLocation(location);
        part.setAutoReorder(autoReorder);
        return part;
    }

    private static Customer customer(
            String id,
            String name,
            String email,
            String phone,
            String company,
            int totalOrders,
            String lastOrder,
            String totalSpent,
            CustomerStatus status
    ) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setCompany(company);
        customer.setTotalOrders(totalOrders);
        customer.setLastOrder(LocalDate.parse(lastOrder));
        customer.setTotalSpent(new BigDecimal(totalSpent));
        customer.setStatus(status);
        return customer;
    }

    private ReorderEntry reorder(
            String id,
            String partId,
            int quantity,
            String supplier,
            String unitCost,
            ReorderStatus status,
            ReorderType type,
            String createdAt,
            String deliveredAt
    ) {
        Part part = partRepository.findById(partId)
                .orElseThrow(() -> new IllegalStateException("Missing seed part: " + partId));
        ReorderEntry entry = new ReorderEntry();
        entry.setId(id);
        entry.setPart(part);
        entry.setPartSku(part.getSku());
        entry.setPartName(part.getName());
        entry.setQuantity(quantity);
        entry.setSupplier(supplier);
        entry.setUnitCost(new BigDecimal(unitCost));
        entry.setStatus(status);
        entry.setType(type);
        entry.setCreatedAt(LocalDate.parse(createdAt));
        if (deliveredAt != null) {
            entry.setDeliveredAt(LocalDate.parse(deliveredAt));
        }
        return entry;
    }
}
