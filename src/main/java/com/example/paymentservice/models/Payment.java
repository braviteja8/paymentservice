package com.example.paymentservice.models;

import ch.qos.logback.core.joran.action.BaseModelAction;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Payment extends BaseModel{
   private Long amount;
   private PaymentStatus paymentStatus;
   /*
   User who created the payment.
    */
   private Long userId;
   /*
   which order is this payment related to.
    */
   private Long orderId;
   private String paymentLink;
   private String paymentGatewayReferenceId;
   private PaymentGateway paymentGateway;
}
