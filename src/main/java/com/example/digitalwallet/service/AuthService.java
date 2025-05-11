package com.example.digitalwallet.service;

import com.example.digitalwallet.model.ReqRes;
import com.example.digitalwallet.entity.Customer;
import com.example.digitalwallet.repository.CustomerRepository;
import com.example.digitalwallet.util.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

@Service
public class AuthService {

    private final CustomerRepository customerRepository;
    private final JWTUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(CustomerRepository customerRepository, JWTUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.customerRepository = customerRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            Customer customer = new Customer();
            customer.setCustomerId(UUID.randomUUID().toString());
            customer.setIdentityNumber(registrationRequest.getIdentityNumber());
            customer.setName(registrationRequest.getName());
            customer.setSurname(registrationRequest.getSurname());
            customer.setEmail(registrationRequest.getEmail());
            customer.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            customer.setRole(registrationRequest.getRole());

            Customer customerResult = customerRepository.save(customer);
            if (Objects.nonNull(customerResult) && customerResult.getId()>0) {
                resp.setCustomer(customerResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signInRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(), signInRequest.getPassword()));
            var user = customerRepository.findByEmail(signInRequest.getEmail()).orElseThrow();
            System.out.println("USER IS: " + user);
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpressionTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Customer customer = customerRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), customer)) {
            var jwt = jwtUtils.generateToken(customer);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpressionTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        } else {
            response.setStatusCode(500);
            response.setError("Invalid token");
        }
        return response;
    }

}
