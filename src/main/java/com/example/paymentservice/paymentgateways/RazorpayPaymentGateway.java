package com.example.paymentservice.paymentgateways;

import com.example.paymentservice.models.PaymentStatus;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
//import com.scaler.paymentservice12apr.models.PaymentStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RazorpayPaymentGateway implements PaymentGatewayInterface {
    @Autowired
    private RazorpayClient razorpayClient;

//    public RazorpayPaymentGateway(RazorpayClient razorpayClient) {
//        this.razorpayClient = razorpayClient;
//    }


    @Override
    public String createPaymentLink(Long amount, String userName, String userEmail, String userPhone, Long orderId) {
        //razorpay java sdk link
        //add unambigous imports on the fly.->settings
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount",amount);
        paymentLinkRequest.put("currency","INR");
        paymentLinkRequest.put("accept_partial",false);
//        paymentLinkRequest.put("first_min_partial_amount",100);
        /*
        Java get currentTime.->epoch time stamp
        currentTime+30mins.
         */
        paymentLinkRequest.put("expire_by",System.currentTimeMillis()/1000 + 30 * 60); // epoch timestamp
        paymentLinkRequest.put("reference_id",orderId.toString());
//      paymentLinkRequest.put("description","Payment for policy no #23456");
        JSONObject customer = new JSONObject();
        customer.put("name",userName);
        customer.put("contact",userPhone);
        customer.put("email",userEmail);
        paymentLinkRequest.put("customer",customer);
        JSONObject notify = new JSONObject();
        notify.put("sms",true);
        notify.put("email",true);
        paymentLinkRequest.put("reminder_enable",true);
//        JSONObject notes = new JSONObject();
//        notes.put("policy_name","Jeevan Bima");
//        paymentLinkRequest.put("notes",notes);
        /*
        When i will get redirected to scaler.com,in the parameters
        i have payment_id,using this id i can check the status of
        the payment.
         */
        paymentLinkRequest.put("callback_url","https://www.scaler.com/academy/mentee-dashboard/todos");
        paymentLinkRequest.put("callback_method","get");
        PaymentLink payment = null;
        try {
            payment = razorpayClient.paymentLink.create(paymentLinkRequest);
        } catch (RazorpayException e) {
            throw new RuntimeException(e);
        }

        return payment.get("short_url");
    }

    @Override
    public PaymentStatus getPaymentStatus(String paymentId)  {
        Payment payment = null;

        try {
            payment = razorpayClient.payments.fetch(paymentId);
        } catch (RazorpayException razorpayException) {
            System.out.println(razorpayException);
            System.out.println("Something went wrong");
        }

        String paymentStatus = payment.get("status");

        if (paymentStatus.equals("captured")) {
            return PaymentStatus.SUCCESS;
        } else if (paymentStatus.equals("failed")) {
            return PaymentStatus.FAILURE;
        }
        return null;
    }
}