package org.example.repository;

import org.example.entity.Customer;
import org.example.entity.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    List<Customer> findByStatus(CustomerStatus status);

    @Query("""
            SELECT c FROM Customer c
            WHERE (:status IS NULL OR c.status = :status)
              AND (
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(c.email) LIKE LOWER(CONCAT('%', :query, '%'))
                OR LOWER(c.company) LIKE LOWER(CONCAT('%', :query, '%'))
              )
            """)
    List<Customer> search(String query, CustomerStatus status);

    long countByStatus(CustomerStatus status);
}
