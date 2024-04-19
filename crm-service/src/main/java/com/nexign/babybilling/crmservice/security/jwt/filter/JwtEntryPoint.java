package com.nexign.babybilling.crmservice.security.jwt.filter;

import com.google.gson.Gson;
import com.nexign.babybilling.payload.response.BaseMessageResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String responseJson;
        if(e instanceof BadCredentialsException) {
            responseJson = new Gson().toJson(BaseMessageResponse.builder()
                            .message("Неверный логин или пароль")
                    .build());
        } else {
            responseJson = new Gson().toJson(BaseMessageResponse.builder()
                    .message("Доступ запрещен")
                    .build());
        }

        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().println(responseJson);
    }
}
