package org.example.service;

import org.example.dto.ReorderRequest;
import org.example.dto.ReorderResponse;
import org.example.dto.ReorderStatusUpdateRequest;
import org.example.entity.Part;
import org.example.entity.ReorderEntry;
import org.example.entity.ReorderStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.ReorderEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ReorderService {

    private final ReorderEntryRepository reorderEntryRepository;
    private final PartService partService;

    public ReorderService(ReorderEntryRepository reorderEntryRepository, PartService partService) {
        this.reorderEntryRepository = reorderEntryRepository;
        this.partService = partService;
    }

    @Transactional(readOnly = true)
    public List<ReorderResponse> findAll(ReorderStatus status) {
        List<ReorderEntry> entries = status == null
                ? reorderEntryRepository.findAll()
                : reorderEntryRepository.findByStatus(status);
        return entries.stream().map(ReorderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ReorderResponse findById(String id) {
        return ReorderResponse.from(getEntry(id));
    }

    public ReorderResponse create(ReorderRequest request) {
        Part part = partService.getPart(request.partId());
        ReorderEntry entry = new ReorderEntry();
        entry.setId("r" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        entry.setPart(part);
        entry.setPartSku(part.getSku());
        entry.setPartName(part.getName());
        entry.setQuantity(request.quantity());
        entry.setSupplier(request.supplier() == null || request.supplier().isBlank()
                ? part.getSupplier()
                : request.supplier());
        entry.setUnitCost(request.unitCost() == null ? part.getUnitPrice() : request.unitCost());
        entry.setStatus(ReorderStatus.pending);
        entry.setType(request.type());
        entry.setCreatedAt(LocalDate.now());
        return ReorderResponse.from(reorderEntryRepository.save(entry));
    }

    public ReorderResponse updateStatus(String id, ReorderStatusUpdateRequest request) {
        ReorderEntry entry = getEntry(id);
        ReorderStatus previous = entry.getStatus();
        ReorderStatus next = request.status();

        if (previous == next) {
            return ReorderResponse.from(entry);
        }

        entry.setStatus(next);

        if (next == ReorderStatus.delivered && previous != ReorderStatus.delivered) {
            entry.setDeliveredAt(LocalDate.now());
            Part part = entry.getPart();
            part.setStock(part.getStock() + entry.getQuantity());
        } else if (previous == ReorderStatus.delivered && next != ReorderStatus.delivered) {
            Part part = entry.getPart();
            part.setStock(Math.max(0, part.getStock() - entry.getQuantity()));
            entry.setDeliveredAt(null);
        }

        return ReorderResponse.from(reorderEntryRepository.save(entry));
    }

    public void delete(String id) {
        if (!reorderEntryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reorder entry not found: " + id);
        }
        reorderEntryRepository.deleteById(id);
    }

    private ReorderEntry getEntry(String id) {
        return reorderEntryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reorder entry not found: " + id));
    }
}
