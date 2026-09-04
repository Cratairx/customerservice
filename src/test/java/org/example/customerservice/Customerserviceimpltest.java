package org.example.customerservice;

import org.example.customerservice.model.Customer;
import org.example.customerservice.repositories.CustomerRepository;
import org.example.customerservice.services.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CustomerServiceImpl's business logic.
 * No Spring context is started here — Mockito wires the mocks directly,
 * which is faster and keeps this test focused on the service logic itself.
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerServiceImpl;

    @Test
    void registerShouldReturnNullWhenEmailAlreadyInUse() {
        when(customerRepository.findByEmail("john@example.com"))
                .thenReturn(Optional.of(new Customer()));

        Customer result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertNull(result);
        verify(customerRepository, never()).save(any());
    }

    @Test
    void registerShouldReturnCustomerWhenEmailNotInUse() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.empty());

        Customer result = customerServiceImpl.register("John", "Doe", "john@example.com");

        assertNotNull(result);
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void updateCustomerShouldUpdateFields() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerServiceImpl.updateCustomer(1L, "Emil", "D", "emil@example.com");

        assertEquals("Emil", customer.getFirstName());
        assertEquals("D", customer.getLastName());
        assertEquals("emil@example.com", customer.getEmail());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void getCustomerByIdShouldReturnCustomer() {
        Customer customer = new Customer();
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setId(1L);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        assertEquals(customer, customerServiceImpl.getCustomerById(1L));
    }
}
