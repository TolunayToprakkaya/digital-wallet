package com.example.digitalwallet.model;

import com.example.digitalwallet.entity.Customer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {
    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expressionTime;
    private Long identityNumber;
    private String name;
    private String surname;
    private String email;
    private String role;
    private String password;
    private Customer customer;
}
