package org.example.service;

import org.example.dto.PartRequest;
import org.example.dto.PartResponse;
import org.example.entity.Part;
import org.example.exception.ConflictException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.PartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartServiceTest {

    @Mock
    private PartRepository partRepository;

    @InjectMocks
    private PartService partService;

    private Part existing;

    @BeforeEach
    void setUp() {
        existing = new Part();
        existing.setId("p1");
        existing.setSku("ENG-OIL-001");
        existing.setName("Oil Filter");
        existing.setCategory("Engine");
        existing.setStock(45);
        existing.setThreshold(20);
        existing.setReorderQty(100);
        existing.setUnitPrice(new BigDecimal("8.99"));
        existing.setMarkupPct(new BigDecimal("40"));
        existing.setLabourCost(new BigDecimal("20"));
        existing.setSupplier("Bosch");
        existing.setLocation("A1-01");
        existing.setAutoReorder(true);
    }

    @Test
    void findAllReturnsMappedParts() {
        when(partRepository.findAll()).thenReturn(List.of(existing));

        List<PartResponse> result = partService.findAll(null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).sku()).isEqualTo("ENG-OIL-001");
        assertThat(result.get(0).status()).isEqualTo("ok");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(partRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> partService.findById("missing"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("missing");
    }

    @Test
    void createRejectsDuplicateSku() {
        PartRequest request = sampleRequest();
        when(partRepository.existsBySkuIgnoreCase("ENG-OIL-001")).thenReturn(true);

        assertThatThrownBy(() -> partService.create(request))
                .isInstanceOf(ConflictException.class);
    }

    @Test
    void createPersistsNewPart() {
        PartRequest request = sampleRequest();
        when(partRepository.existsBySkuIgnoreCase("ENG-OIL-001")).thenReturn(false);
        when(partRepository.save(any(Part.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PartResponse response = partService.create(request);

        ArgumentCaptor<Part> captor = ArgumentCaptor.forClass(Part.class);
        verify(partRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).startsWith("p");
        assertThat(response.sku()).isEqualTo("ENG-OIL-001");
        assertThat(response.status()).isEqualTo("ok");
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(partRepository.existsById("p9")).thenReturn(false);

        assertThatThrownBy(() -> partService.delete("p9"))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private static PartRequest sampleRequest() {
        return new PartRequest(
                "ENG-OIL-001",
                "Oil Filter",
                "Engine",
                45,
                20,
                100,
                new BigDecimal("8.99"),
                new BigDecimal("40"),
                new BigDecimal("20"),
                "Bosch",
                "A1-01",
                true
        );
    }
}
