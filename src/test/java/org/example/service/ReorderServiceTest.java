package org.example.service;

import org.example.dto.ReorderRequest;
import org.example.dto.ReorderResponse;
import org.example.dto.ReorderStatusUpdateRequest;
import org.example.entity.Part;
import org.example.entity.ReorderEntry;
import org.example.entity.ReorderStatus;
import org.example.entity.ReorderType;
import org.example.repository.ReorderEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReorderServiceTest {

    @Mock
    private ReorderEntryRepository reorderEntryRepository;

    @Mock
    private PartService partService;

    @InjectMocks
    private ReorderService reorderService;

    private Part part;
    private ReorderEntry entry;

    @BeforeEach
    void setUp() {
        part = new Part();
        part.setId("p6");
        part.setSku("BRK-ROT-006");
        part.setName("Brake Rotor (Front)");
        part.setSupplier("Brembo");
        part.setUnitPrice(new BigDecimal("72.00"));
        part.setStock(0);

        entry = new ReorderEntry();
        entry.setId("r1");
        entry.setPart(part);
        entry.setPartSku(part.getSku());
        entry.setPartName(part.getName());
        entry.setQuantity(20);
        entry.setSupplier("Brembo");
        entry.setUnitCost(new BigDecimal("72.00"));
        entry.setStatus(ReorderStatus.pending);
        entry.setType(ReorderType.manual);
        entry.setCreatedAt(LocalDate.of(2026, 7, 13));
    }

    @Test
    void createSnapshotsPartDetails() {
        when(partService.getPart("p6")).thenReturn(part);
        when(reorderEntryRepository.save(any(ReorderEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReorderResponse response = reorderService.create(new ReorderRequest(
                "p6",
                20,
                null,
                null,
                ReorderType.manual
        ));

        assertThat(response.partSku()).isEqualTo("BRK-ROT-006");
        assertThat(response.supplier()).isEqualTo("Brembo");
        assertThat(response.unitCost()).isEqualByComparingTo("72.00");
        assertThat(response.status()).isEqualTo(ReorderStatus.pending);
    }

    @Test
    void deliveringIncrementsPartStock() {
        when(reorderEntryRepository.findById("r1")).thenReturn(Optional.of(entry));
        when(reorderEntryRepository.save(any(ReorderEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReorderResponse response = reorderService.updateStatus(
                "r1",
                new ReorderStatusUpdateRequest(ReorderStatus.delivered)
        );

        assertThat(response.status()).isEqualTo(ReorderStatus.delivered);
        assertThat(response.deliveredAt()).isNotNull();
        assertThat(part.getStock()).isEqualTo(20);
    }

    @Test
    void undoingDeliveryDecrementsStock() {
        entry.setStatus(ReorderStatus.delivered);
        entry.setDeliveredAt(LocalDate.of(2026, 7, 14));
        part.setStock(20);
        when(reorderEntryRepository.findById("r1")).thenReturn(Optional.of(entry));
        when(reorderEntryRepository.save(any(ReorderEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

        reorderService.updateStatus("r1", new ReorderStatusUpdateRequest(ReorderStatus.cancelled));

        assertThat(part.getStock()).isZero();
        assertThat(entry.getDeliveredAt()).isNull();
    }
}
