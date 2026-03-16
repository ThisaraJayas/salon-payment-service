package com.salon.payment.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreatePaymentRequest {
    @NotBlank(message = "Booking ID is required")
    private String bookingId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String currency = "USD";

    @NotBlank(message = "Variant ID is required")
    private String variantId; // LemonSqueezy variant ID for the service
}
