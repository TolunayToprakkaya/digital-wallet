package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerDetailsServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDetailsService customerDetailsService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setPassword("encodedPassword");
    }

    @Test
    void loadUserByUsername_shouldReturnCustomer_whenEmailExists() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        UserDetails result = customerDetailsService.loadUserByUsername("test@example.com");

        assertEquals(customer, result);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenEmailNotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            customerDetailsService.loadUserByUsername("notfound@example.com");
        });
    }

    @Test
    void findByEmail_shouldReturnCustomer_whenEmailExists() {
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

        Customer result = customerDetailsService.findByEmail("test@example.com");

        assertEquals(customer, result);
    }

    @Test
    void findByEmail_shouldThrowException_whenEmailNotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            customerDetailsService.findByEmail("notfound@example.com");
        });
    }
}

