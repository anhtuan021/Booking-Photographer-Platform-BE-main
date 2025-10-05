package org.bookingplatform.service;

import org.bookingplatform.dto.booking.*;

import java.math.BigInteger;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(String token, CreateBookingRequest request);

    List<BookingResponse> getMyBookings(String token);

    BookingResponse getBookingById(String bookingId);

    List<BookingResponse> getAllPhotographerBookings(String token);

    List<BookingResponse> getAll();

    void approveBooking(String token, BigInteger bookingId);

    void rejectBooking(String token, BigInteger bookingId, String reason);

    Object getPhotographerBookingStats(String token);

}
