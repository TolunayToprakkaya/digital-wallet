package com.example.digitalwallet.controller;

import com.example.digitalwallet.model.ReqRes;
import com.example.digitalwallet.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSignUp() {
        ReqRes request = new ReqRes();
        request.setEmail("user@gmail");
        request.setPassword("pass");

        ReqRes expectedResponse = new ReqRes();
        expectedResponse.setToken("jwt-token");

        when(authService.signUp(request)).thenReturn(expectedResponse);

        ResponseEntity<ReqRes> response = authController.signUp(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).signUp(request);
    }

    @Test
    public void testSignIn() {
        ReqRes request = new ReqRes();
        request.setEmail("user@gmail");
        request.setPassword("pass");

        ReqRes expectedResponse = new ReqRes();
        expectedResponse.setToken("access-token");

        when(authService.signIn(request)).thenReturn(expectedResponse);

        ResponseEntity<ReqRes> response = authController.signIn(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).signIn(request);
    }

    @Test
    public void testRefreshToken() {
        ReqRes request = new ReqRes();
        request.setRefreshToken("refresh-token");

        ReqRes expectedResponse = new ReqRes();
        expectedResponse.setToken("new-access-token");

        when(authService.refreshToken(request)).thenReturn(expectedResponse);

        ResponseEntity<ReqRes> response = authController.refreshToken(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedResponse, response.getBody());
        verify(authService).refreshToken(request);
    }
}

