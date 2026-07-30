package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.ReorderRequest;
import org.example.dto.ReorderResponse;
import org.example.dto.ReorderStatusUpdateRequest;
import org.example.entity.ReorderStatus;
import org.example.service.ReorderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reorders")
public class ReorderController {

    private final ReorderService reorderService;

    public ReorderController(ReorderService reorderService) {
        this.reorderService = reorderService;
    }

    @GetMapping
    public List<ReorderResponse> list(@RequestParam(required = false) ReorderStatus status) {
        return reorderService.findAll(status);
    }

    @GetMapping("/{id}")
    public ReorderResponse get(@PathVariable String id) {
        return reorderService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ReorderResponse create(@Valid @RequestBody ReorderRequest request) {
        return reorderService.create(request);
    }

    @PatchMapping("/{id}/status")
    public ReorderResponse updateStatus(
            @PathVariable String id,
            @Valid @RequestBody ReorderStatusUpdateRequest request
    ) {
        return reorderService.updateStatus(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        reorderService.delete(id);
    }
}
