package org.example.service;

import org.example.dto.CustomerRequest;
import org.example.dto.CustomerResponse;
import org.example.dto.CustomerStatsResponse;
import org.example.entity.Customer;
import org.example.entity.CustomerStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream().map(CustomerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse findById(String id) {
        return CustomerResponse.from(getCustomer(id));
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> search(String query, CustomerStatus status) {
        String normalizedQuery = query == null ? "" : query.trim();
        if (normalizedQuery.isEmpty() && status == null) {
            return findAll();
        }
        if (normalizedQuery.isEmpty()) {
            return customerRepository.findByStatus(status).stream().map(CustomerResponse::from).toList();
        }
        return customerRepository.search(normalizedQuery, status).stream().map(CustomerResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public CustomerStatsResponse getStats() {
        List<Customer> customers = customerRepository.findAll();
        long totalOrders = customers.stream().mapToLong(Customer::getTotalOrders).sum();
        BigDecimal totalRevenue = customers.stream()
                .map(Customer::getTotalSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CustomerStatsResponse(
                customers.size(),
                customerRepository.countByStatus(CustomerStatus.active),
                totalOrders,
                totalRevenue
        );
    }

    public CustomerResponse create(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setId("c" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        applyRequest(customer, request);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    public CustomerResponse update(String id, CustomerRequest request) {
        Customer customer = getCustomer(id);
        applyRequest(customer, request);
        return CustomerResponse.from(customerRepository.save(customer));
    }

    public void delete(String id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Customer not found: " + id);
        }
        customerRepository.deleteById(id);
    }

    private Customer getCustomer(String id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + id));
    }

    private void applyRequest(Customer customer, CustomerRequest request) {
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customer.setCompany(request.company());
        customer.setTotalOrders(request.totalOrders());
        customer.setLastOrder(request.lastOrder());
        customer.setTotalSpent(request.totalSpent());
        customer.setStatus(request.status());
    }
}
