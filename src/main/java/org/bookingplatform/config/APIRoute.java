package org.bookingplatform.config;

import lombok.Getter;

@Getter
public class APIRoute {
    public static final String BASE_API = "/api/v1";

    // Authentication APIs
    public static final String AUTH = BASE_API + "/auth";
    public static final String LOGIN = AUTH + "/login";
    public static final String REGISTER = AUTH + "/register";
    public static final String FORGOT_PASSWORD = AUTH + "/forgot-password";
    public static final String RESET_PASSWORD = AUTH + "/reset-password";
    public static final String CHANGE_PASSWORD = AUTH + "/change-password";
    public static final String LOGOUT = AUTH + "/logout";

    // Profiles APIs
    public static final String PROFILES = BASE_API + "/profiles";
    public static final String PROFILE_ME = PROFILES + "/me";

    // Photographer APIs and Portfolio APIs
    public static final String PHOTOGRAPHERS = BASE_API + "/photographers";
    public static final String PORTFOLIOS = BASE_API + "/portfolios";

    // Service Package APIs
    public static final String SERVICE_PACKAGES = BASE_API + "/packages";

    // Booking APIs
    public static final String BOOKINGS = BASE_API + "/bookings";

    // Feedback APIs
    public static final String FEEDBACKS = BASE_API + "/feedbacks";

    // Admin APIs
    public static final String ADMIN = BASE_API + "/admin";
}
