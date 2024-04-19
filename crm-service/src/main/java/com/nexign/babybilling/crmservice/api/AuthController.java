package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.payload.request.AuthRequest;
import com.nexign.babybilling.crmservice.payload.request.RegisterRequest;
import com.nexign.babybilling.crmservice.payload.response.AuthResponse;
import com.nexign.babybilling.crmservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("signin")
    public AuthResponse auth(@Valid @RequestBody AuthRequest request) {
        return authService.doAuth(request);
    }

    @PostMapping("signup")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
