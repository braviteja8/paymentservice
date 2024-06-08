package com.example.paymentservice.paymentgateways;

import com.example.paymentservice.models.PaymentStatus;
import org.springframework.stereotype.Service;

//Strategy design pattern
@Service
public interface PaymentGatewayInterface {
    String createPaymentLink(Long amount,
                             String userName,
                             String userEmail,
                             String userPhone,
                             Long orderId);
    PaymentStatus getPaymentStatus(String paymentId);
}
