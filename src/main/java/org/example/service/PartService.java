package org.example.service;

import org.example.dto.PartRequest;
import org.example.dto.PartResponse;
import org.example.entity.Part;
import org.example.exception.ConflictException;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.PartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PartService {

    private final PartRepository partRepository;

    public PartService(PartRepository partRepository) {
        this.partRepository = partRepository;
    }

    @Transactional(readOnly = true)
    public List<PartResponse> findAll(String category, String search) {
        List<Part> parts;
        if (search != null && !search.isBlank()) {
            parts = partRepository.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(search, search);
        } else if (category != null && !category.isBlank()) {
            parts = partRepository.findByCategoryIgnoreCase(category);
        } else {
            parts = partRepository.findAll();
        }
        return parts.stream().map(PartResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PartResponse findById(String id) {
        return PartResponse.from(getPart(id));
    }

    public PartResponse create(PartRequest request) {
        if (partRepository.existsBySkuIgnoreCase(request.sku())) {
            throw new ConflictException("Part with SKU already exists: " + request.sku());
        }
        Part part = new Part();
        part.setId("p" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        applyRequest(part, request);
        return PartResponse.from(partRepository.save(part));
    }

    public PartResponse update(String id, PartRequest request) {
        Part part = getPart(id);
        partRepository.findBySkuIgnoreCase(request.sku())
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new ConflictException("Part with SKU already exists: " + request.sku());
                });
        applyRequest(part, request);
        return PartResponse.from(partRepository.save(part));
    }

    public void delete(String id) {
        if (!partRepository.existsById(id)) {
            throw new ResourceNotFoundException("Part not found: " + id);
        }
        partRepository.deleteById(id);
    }

    Part getPart(String id) {
        return partRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Part not found: " + id));
    }

    private void applyRequest(Part part, PartRequest request) {
        part.setSku(request.sku());
        part.setName(request.name());
        part.setCategory(request.category());
        part.setStock(request.stock());
        part.setThreshold(request.threshold());
        part.setReorderQty(request.reorderQty());
        part.setUnitPrice(request.unitPrice());
        part.setMarkupPct(request.markupPct());
        part.setLabourCost(request.labourCost());
        part.setSupplier(request.supplier());
        part.setLocation(request.location());
        part.setAutoReorder(request.autoReorder());
    }
}
