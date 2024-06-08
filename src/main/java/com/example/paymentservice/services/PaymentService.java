package com.example.paymentservice.services;

import com.example.paymentservice.models.Payment;
import com.example.paymentservice.models.PaymentGateway;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.paymentgateways.PaymentGatewayFactory;
import com.example.paymentservice.paymentgateways.PaymentGatewayInterface;
import com.example.paymentservice.paymentgateways.RazorpayPaymentGateway;
import com.example.paymentservice.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private PaymentGatewayFactory paymentGatewayFactory;
    private RazorpayPaymentGateway razorpayPaymentGateway;
    private PaymentRepository paymentRepository;

    public PaymentService(PaymentGatewayFactory paymentGatewayFactory, PaymentRepository paymentRepository, RazorpayPaymentGateway razorpayPaymentGateway) {
        this.paymentGatewayFactory = paymentGatewayFactory;
        this.paymentRepository = paymentRepository;
        this.razorpayPaymentGateway = razorpayPaymentGateway;
    }

    public String createPaymentLink(Long orderId) {
        // I need to get the details of the order.(amount)
        /*
        I assume it's already written code.
        Order order=restTemplate.getForObject("",Order.class);
        Long amount=order.getAmount();
        String userName=order.getUser().getName();
        String userMobile=order.getUser().getPhoneNumber();
        String userEmail=order.getUser().getEmail();
                 */
        /*
        1000L->discussed in class.
         */
        Long amount = 1000L;
        String userName = "Raviteja";
        String userMobile = "+918383838383";
        String userEmail = "abc@gmail.com";
           /*
        Below i am hardcoded razorpaymentGateway,
        But in reality it is choosen at that particular time
        which is having highest success rate in the partnerships
        they had.
         */
        PaymentGatewayInterface paymentGateway = paymentGatewayFactory.getBestPaymentGateway();

        String paymentLink = paymentGateway.createPaymentLink(amount,
                userName,
                userEmail,
                userMobile,
                orderId);

        Payment payment = new Payment();
        payment.setPaymentLink(paymentLink);
        payment.setOrderId(orderId);
        payment.setPaymentGateway(PaymentGateway.RAZORPAY);
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setAmount(amount);
        /*
        How will saving of payment help?
        1.I will ask razorpay to tell the status of payment.



         */
        paymentRepository.save(payment);
        return paymentLink;
    }

    public PaymentStatus getPaymentStatus(String PaymentGatewayPaymentId) {
        Payment payment = paymentRepository.findByPaymentGatewayReferenceId(PaymentGatewayPaymentId);
        PaymentGatewayInterface paymentGateway = null;
        if (payment.getPaymentGateway().equals(PaymentGateway.RAZORPAY)) {
            paymentGateway = razorpayPaymentGateway;
        }
        PaymentStatus paymentStatus = paymentGateway.getPaymentStatus(PaymentGatewayPaymentId);
        payment.setPaymentStatus(paymentStatus);
        /*
        In the database as well you have updated the
        payment status.
         */
        paymentRepository.save(payment);
        return paymentStatus;
    }
}