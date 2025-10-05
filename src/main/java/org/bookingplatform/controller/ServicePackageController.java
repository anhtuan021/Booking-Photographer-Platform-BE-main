package org.bookingplatform.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bookingplatform.config.APIRoute;
import org.bookingplatform.dto.BaseRequest;
import org.bookingplatform.dto.BaseResponse;
import org.bookingplatform.dto.photographer.CreateServicePackageRequest;
import org.bookingplatform.dto.photographer.ServicePackageResponse;
import org.bookingplatform.service.ServicePackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ServicePackageController {
    private final ServicePackageService servicePackageService;

    @PostMapping(APIRoute.SERVICE_PACKAGES)
    public ResponseEntity<BaseResponse<ServicePackageResponse>> createServicePackage(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody BaseRequest<CreateServicePackageRequest> request) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            ServicePackageResponse response = servicePackageService.createServicePackage(token, request.getRequestParameters());
            return ResponseEntity.ok(new BaseResponse<>(
                    request.getRequestTrace(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    request.getRequestTrace(), "01030004", e.getMessage()));
        }
    }

//    @PutMapping(APIRoute.SERVICE_PACKAGES)
//    public ResponseEntity<BaseResponse<ServicePackageResponse>> updateServicePackage(
//            @RequestHeader("Authorization") String authHeader,
//            @Valid @RequestBody BaseRequest<CreateServicePackageRequest> request) {
//        try {
//            String token = authHeader.startsWith("Bearer ")
//                    ? authHeader.substring(7).trim()
//                    : authHeader.trim();
//
//            ServicePackageResponse response = servicePackageService.updateServicePackage(
//                    token, request.getRequestParameters().getId(), request.getRequestParameters());
//            return ResponseEntity.ok(new BaseResponse<>(
//                    request.getRequestTrace(), response));
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body(new BaseResponse<>(
//                    request.getRequestTrace(), "01030005", e.getMessage()));
//        }
//    }

    @DeleteMapping(APIRoute.SERVICE_PACKAGES + "/{packageId}")
    public ResponseEntity<BaseResponse<String>> deleteServicePackage(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable BigInteger packageId) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            servicePackageService.deleteServicePackage(token, packageId);

            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), "Service package deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030006", e.getMessage()));
        }
    }

    /**
     * Get my service packages
     */
    @GetMapping(APIRoute.SERVICE_PACKAGES + "/me")
    public ResponseEntity<BaseResponse<List<ServicePackageResponse>>> getMyServicePackages(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "isActive", required = false) Boolean isActive) {
        try {
            String token = authHeader.startsWith("Bearer ")
                    ? authHeader.substring(7).trim()
                    : authHeader.trim();

            List<ServicePackageResponse> response = servicePackageService.getMyServicePackages(token, isActive);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030007", e.getMessage()));
        }
    }

    /**
     * Get service package by ID
     */
    @GetMapping(APIRoute.SERVICE_PACKAGES + "/{packageId}")
    public ResponseEntity<BaseResponse<ServicePackageResponse>> getServicePackageById(
            @PathVariable BigInteger packageId) {
        try {
            ServicePackageResponse response = servicePackageService.getServicePackageById(packageId);
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030008", e.getMessage()));
        }
    }

    @GetMapping(APIRoute.SERVICE_PACKAGES)
    public ResponseEntity<BaseResponse<List<ServicePackageResponse>>> getAllServicePackage() {
        try {
            List<ServicePackageResponse> response = servicePackageService.getAll();
            return ResponseEntity.ok(new BaseResponse<>(UUID.randomUUID().toString(), response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(
                    UUID.randomUUID().toString(), "01030009", e.getMessage()));
        }
    }
}
