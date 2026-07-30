package org.example.service;

import org.example.dto.CustomerRequest;
import org.example.dto.CustomerResponse;
import org.example.dto.CustomerStatsResponse;
import org.example.entity.Customer;
import org.example.entity.CustomerStatus;
import org.example.exception.ResourceNotFoundException;
import org.example.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer existing;

    @BeforeEach
    void setUp() {
        existing = new Customer();
        existing.setId("c1");
        existing.setName("Marcus Webb");
        existing.setEmail("m.webb@autoshop.com");
        existing.setPhone("+1 555-0101");
        existing.setCompany("Webb Auto Repair");
        existing.setTotalOrders(47);
        existing.setLastOrder(LocalDate.of(2026, 7, 10));
        existing.setTotalSpent(new BigDecimal("12450"));
        existing.setStatus(CustomerStatus.active);
    }

    @Test
    void getStatsAggregatesCustomers() {
        Customer inactive = new Customer();
        inactive.setId("c6");
        inactive.setTotalOrders(9);
        inactive.setTotalSpent(new BigDecimal("1200"));
        inactive.setStatus(CustomerStatus.inactive);

        when(customerRepository.findAll()).thenReturn(List.of(existing, inactive));
        when(customerRepository.countByStatus(CustomerStatus.active)).thenReturn(1L);

        CustomerStatsResponse stats = customerService.getStats();

        assertThat(stats.totalCustomers()).isEqualTo(2);
        assertThat(stats.activeCustomers()).isEqualTo(1);
        assertThat(stats.totalOrders()).isEqualTo(56);
        assertThat(stats.totalRevenue()).isEqualByComparingTo("13650");
    }

    @Test
    void createReturnsPersistedCustomer() {
        when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerResponse response = customerService.create(new CustomerRequest(
                "New Customer",
                "new@example.com",
                "+1 555-9999",
                "New Co",
                0,
                null,
                BigDecimal.ZERO,
                CustomerStatus.active
        ));

        assertThat(response.id()).startsWith("c");
        assertThat(response.name()).isEqualTo("New Customer");
    }

    @Test
    void findByIdThrowsWhenMissing() {
        when(customerRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.findById("missing"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
