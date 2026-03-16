package com.salon.payment.repository;

import com.salon.payment.model.Payment;
import com.salon.payment.model.PaymentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByBookingId(String bookingId);
    List<Payment> findByStatus(PaymentStatus status);
    Optional<Payment> findByLemonSqueezyOrderId(String orderId);
    Optional<Payment> findByTransactionId(String transactionId);
    Optional<Payment> findByLemonSqueezyCheckoutRequestId(String checkoutRequestId); // New method
}