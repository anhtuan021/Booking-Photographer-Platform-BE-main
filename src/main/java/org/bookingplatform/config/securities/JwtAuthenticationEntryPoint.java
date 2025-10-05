package org.bookingplatform.config.securities;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.bookingplatform.dto.BaseResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        log.error("Unauthorized error: {}", authException.getMessage());

        BaseResponse<Void> body = BaseResponse.<Void>builder()
                .requestTrace(UUID.randomUUID().toString())
                .responseDateTime(LocalDateTime.now())
                .responseStatus(BaseResponse.ResponseStatus.builder()
                        .responseCode("01010002")
                        .responseMessage("Unauthorized: " + authException.getMessage())
                        .build())
                .build();

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}