package org.example.repository;

import org.example.entity.ReorderEntry;
import org.example.entity.ReorderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReorderEntryRepository extends JpaRepository<ReorderEntry, String> {

    List<ReorderEntry> findByStatus(ReorderStatus status);

    List<ReorderEntry> findByPartId(String partId);
}
