package org.bookingplatform.config.securities;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.service.JwtService;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    @SuppressWarnings("RedundantThrows")
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authHeader = request.getHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7).trim();
//            if (token.isEmpty()) {
//                filterChain.doFilter(request, response);
//                return;
//            }

            Claims claims = jwtService.extractAllClaims(token);

            String username = claims.getSubject();
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException ex) {
            log.error("JWT validation error: {}", ex.getMessage());
            sendErrorResponse(response, "01010001", "Authentication failed: " + ex.getMessage());
        } catch (Exception ex) {
            log.error("Unexpected error in JwtAuthenticationFilter: {}", ex.getMessage());
            sendErrorResponse(response, "01019999", "Internal server error");
        }
    }

    private void sendErrorResponse(HttpServletResponse response,
                                   String code,
                                   String message) throws IOException {
        BaseResponse<Void> body = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode(code)
                        .responseMessage(message)
                        .build())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}