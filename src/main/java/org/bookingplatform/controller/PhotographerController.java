package org.bookingplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseRequest;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.photographer.*;
import org.bookingplatform.service.PhotographerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class PhotographerController {
    private final PhotographerService photographerService;

    /**
     * Block availability
     */
    @PostMapping(APIRoute.PHOTOGRAPHERS + "/availability/block")
    public ResponseEntity<BaseResponse<String>> blockAvailability(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BaseRequest<BlockAvailabilityRequest> request) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7)
                    : authHeader;

            photographerService.blockAvailability(token, request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), "Availability blocked successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01030018", e.getMessage()));
        }
    }

    /**
     * Unblock availability
     */
    @DeleteMapping(APIRoute.PHOTOGRAPHERS + "/availability/block")
    public ResponseEntity<BaseResponse<String>> unblockAvailability(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("date") LocalDate date,
            @RequestParam(value = "time", required = false) String time) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7)
                    : authHeader;

            photographerService.unblockAvailability(token, date, time);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), "Availability unblocked successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030019", e.getMessage()));
        }
    }

    /**
     * Get my blocked dates
     */
    @GetMapping(APIRoute.PHOTOGRAPHERS + "/availability/blocked")
    public ResponseEntity<BaseResponse<List<BlockedDateResponse>>> getMyBlockedDates(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "startDate", required = false) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) LocalDate endDate) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7)
                    : authHeader;

            List<BlockedDateResponse> response = photographerService.getMyBlockedDates(token, startDate, endDate);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030020", e.getMessage()));
        }
    }
}