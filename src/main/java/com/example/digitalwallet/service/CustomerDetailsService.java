package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.repository.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomerDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return customerRepository.findByEmail(username).orElseThrow();
    }

    public Customer findByEmail(String customerEmail) {
        return customerRepository.findByEmail(customerEmail).orElseThrow();
    }

}
