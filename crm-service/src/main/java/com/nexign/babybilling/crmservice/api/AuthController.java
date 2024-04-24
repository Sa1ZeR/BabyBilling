package com.nexign.babybilling.crmservice.api;

import com.nexign.babybilling.crmservice.payload.request.AuthRequest;
import com.nexign.babybilling.crmservice.payload.request.RegisterRequest;
import com.nexign.babybilling.crmservice.payload.response.AuthResponse;
import com.nexign.babybilling.crmservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    @Operation(description = "Авторизация пользователя")
    @ApiResponse(description = "jwt токен, который необходим для дальнейших действий в системе")
    @PostMapping("signin")
    public AuthResponse auth(@Valid @RequestBody @ParameterObject AuthRequest request) {
        return authService.doAuth(request);
    }

    @Operation(description = "Регистрация пользователя")
    @PostMapping("signup")
    public ResponseEntity<?> register(@Valid @RequestBody @ParameterObject RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
}
