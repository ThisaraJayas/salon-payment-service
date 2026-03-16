package com.salon.payment.dto;

import lombok.Data;

@Data
public class BookingUpdateRequest {
    private String bookingId;
    private String paymentId;
    private String paymentStatus;
    private String transactionId;
    private String lemonSqueezyOrderId;
}