package com.nexign.babybilling.crmservice.security.jwt;

import com.nexign.babybilling.crmservice.service.UserDetailsServiceImpl;
import com.nexign.babybilling.payload.dto.CustomerDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.HttpHeaders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static io.jsonwebtoken.Jwts.SIG.HS512;

@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String TOKEN_PREFIX = "Bearer_";


    private final UserDetailsServiceImpl userDetailsService;
    @Value("${jwt.duration}")
    private long durationToken;
    private Key secretKey;

    /**
     * Генерация jwt токена для пользователя
     * @param customer - абонент
     * @return jwt token
     */
    public String generateToken(CustomerDto customer) {
        Date now = new Date(System.currentTimeMillis());
        Date expiredTime = new Date(now.getTime() + durationToken * 1000);

        Map<String, Object> claims = new HashMap<>();
        claims.put("misisnd",  customer.msisnd());

        return TOKEN_PREFIX + Jwts.builder()
                .issuedAt(now)
                .expiration(expiredTime)
                .claims(claims)
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Проверка валидности токена
     * @param token токен
     * @return статус валидности
     */
    public boolean isValidToken(String token) {
        try {
            Jws<Claims> jwtClaims = parseToken(token);

            if(jwtClaims.getPayload().getExpiration().before(new Date())) {
                return false;
            }
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    /**
     * Извлечение токена из http заголовка
     * @param request {@link HttpServletRequest}
     * @return токен или null, если токен отсутсвует
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.hasText(token) && token.startsWith(TOKEN_PREFIX)) {
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * Создание {@link Authentication} на основе токена
     * @param token токен
     * @return объект авторизации
     */
    public Authentication getAuthentication(String token) {
        UserDetails user = userDetailsService.loadUserByUsername(getUserMsisnd(token));

        return new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
    }

    /**
     * Получение номера абонента из токена
     * @param token токен
     * @return номер абонента
     */
    public String getUserMsisnd(String token) {
        Jws<Claims> jwtClaims = parseToken(token);

        return (String) jwtClaims.getPayload().get("misisnd");
    }

    /**
     * Получение Claims по jwt токену
     * @param token
     * @return Claims
     */
    private Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignInKey())
                .build().parseSignedClaims(token);
    }

    /**
     * Создание ключа для подписи jwt токена
     * @return ключ {@link Key}
     */
    private Key getSignInKey() {
        if(secretKey == null)
            secretKey = HS512.key().build();

        return secretKey;
    }
}
