package org.example.repository;

import org.example.entity.Part;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PartRepository extends JpaRepository<Part, String> {

    Optional<Part> findBySkuIgnoreCase(String sku);

    boolean existsBySkuIgnoreCase(String sku);

    List<Part> findByCategoryIgnoreCase(String category);

    List<Part> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku);
}
