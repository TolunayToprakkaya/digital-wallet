package com.example.digitalwallet.service;

import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.model.ReqRes;
import com.example.digitalwallet.repository.CustomerRepository;
import com.example.digitalwallet.util.JWTUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private CustomerRepository customerRepository;
    private JWTUtils jwtUtils;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        jwtUtils = mock(JWTUtils.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);

        authService = new AuthService(customerRepository, jwtUtils, passwordEncoder, authenticationManager);
    }

    @Test
    void testSignUpSuccess() {
        ReqRes request = new ReqRes();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setIdentityNumber(12345678900L);
        request.setName("Test");
        request.setSurname("User");
        request.setRole("USER");

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1);

        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

        ReqRes response = authService.signUp(request);

        assertEquals(200, response.getStatusCode());
        assertNotNull(response.getCustomer());
        assertEquals("User Saved Successfully", response.getMessage());
    }

    @Test
    void testSignInSuccess() {
        ReqRes request = new ReqRes();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        Customer user = new Customer();
        user.setEmail("test@example.com");

        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateToken(user)).thenReturn("jwtToken");
        when(jwtUtils.generateRefreshToken(any(), eq(user))).thenReturn("refreshToken");

        ReqRes response = authService.signIn(request);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken("test@example.com", "password123"));

        assertEquals(200, response.getStatusCode());
        assertEquals("jwtToken", response.getToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals("Successfully Signed In", response.getMessage());
    }

    @Test
    void testRefreshTokenSuccess() {
        String token = "validToken";
        ReqRes request = new ReqRes();
        request.setToken(token);

        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        when(jwtUtils.extractUsername(token)).thenReturn("test@example.com");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(jwtUtils.isTokenValid(token, customer)).thenReturn(true);
        when(jwtUtils.generateToken(customer)).thenReturn("newJwtToken");

        ReqRes response = authService.refreshToken(request);

        assertEquals(200, response.getStatusCode());
        assertEquals("newJwtToken", response.getToken());
        assertEquals(token, response.getRefreshToken());
    }

    @Test
    void testSignUpThrowsException() {
        ReqRes request = new ReqRes();
        request.setEmail("error@example.com");

        when(customerRepository.save(any())).thenThrow(new RuntimeException("Database error"));

        ReqRes response = authService.signUp(request);

        assertEquals(500, response.getStatusCode());
        assertEquals("Database error", response.getError());
    }

    @Test
    void testSignInThrowsException() {
        ReqRes request = new ReqRes();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        doThrow(new RuntimeException("Authentication failed"))
                .when(authenticationManager)
                .authenticate(any());

        ReqRes response = authService.signIn(request);

        assertEquals(500, response.getStatusCode());
        assertEquals("Authentication failed", response.getError());
    }

    @Test
    void testRefreshTokenInvalidToken() {
        String token = "invalidToken";
        ReqRes request = new ReqRes();
        request.setToken(token);

        Customer customer = new Customer();
        customer.setEmail("test@example.com");

        when(jwtUtils.extractUsername(token)).thenReturn("test@example.com");
        when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));
        when(jwtUtils.isTokenValid(token, customer)).thenReturn(false);

        ReqRes response = authService.refreshToken(request);

        assertEquals(500, response.getStatusCode());
        assertEquals("Invalid token", response.getError());
    }


}

