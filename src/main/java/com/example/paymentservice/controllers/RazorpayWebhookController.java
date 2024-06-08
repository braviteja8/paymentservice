package com.example.paymentservice.controllers;

import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks/razorpay")
public class RazorpayWebhookController {
   @Autowired
   private RazorpayClient razorpayClient;
   @PostMapping ()
   public void handleWebhookEvent(){
      /*
      1.update the status in the database.
      2.send email etc.
       */

      System.out.println("hi");
   }
}
