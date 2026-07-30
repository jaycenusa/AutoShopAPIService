package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.PartRequest;
import org.example.dto.PartResponse;
import org.example.service.PartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/parts")
public class PartController {

    private final PartService partService;

    public PartController(PartService partService) {
        this.partService = partService;
    }

    @GetMapping
    public List<PartResponse> list(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search
    ) {
        return partService.findAll(category, search);
    }

    @GetMapping("/{id}")
    public PartResponse get(@PathVariable String id) {
        return partService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PartResponse create(@Valid @RequestBody PartRequest request) {
        return partService.create(request);
    }

    @PutMapping("/{id}")
    public PartResponse update(@PathVariable String id, @Valid @RequestBody PartRequest request) {
        return partService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        partService.delete(id);
    }
}
