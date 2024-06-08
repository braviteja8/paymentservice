package com.example.paymentservice.controllers;

import com.example.paymentservice.dtos.CreatePaymentLinkRequestDto;
import com.example.paymentservice.dtos.CreatePaymentLinkResponseDto;
import com.example.paymentservice.models.PaymentStatus;
import com.example.paymentservice.services.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService){
        this.paymentService=paymentService;
    }

    /*
    If the frontend/user sends amount as the part of the request,
    people try to modify the code of frontend and try to misuse it.
    ps should talk to os and get the amount by using orderId.
     */
    @PostMapping()
    public CreatePaymentLinkResponseDto createPaymentLink(@RequestBody CreatePaymentLinkRequestDto requestDto){
        String url=this.paymentService.createPaymentLink(requestDto.getOrderId());
        CreatePaymentLinkResponseDto response=new CreatePaymentLinkResponseDto();
        response.setUrl(url);
        return response;
    }
    /*
    When will someone send the req to this api;
    ////1.46
    ->when the person redirects to callback url
    in that url there will be paymentId present there.
    -> callback url pages sends a req to this to check
    the status of the payment.
    1.where database is updated and you can send email
    send notifications etc.
    GREEN MESSAGE,RED MESSAGE YOU HAVE SEEN IN
    AMAZON.
    WHAT IF A PERSON NEVER GETS REDIRECTED TO
    PARTICULAR CALLBACK PAGE?
    ->DATABASE WILL NEVER KNOW PAYMENTSTATUS.
    SOLUTION FOR THIS IS:
    WEBHOOKS->pg directly sends the request to ps.
    ////151
    ->Do u think razorpay will able to send a req to
    localhost:8083/webhooks/razorpay->No
    ->To do this deploy ps at a particular place,after deploying
    we ask razorpay to send req to that place.
    ->paymentservice.naman.dev/webhooks/razorpay->very slow.
    becoz to deploy ps and do all these it will take time.
    solution->LOCAL TUNNEL
    it creates url of ps hosted at 8083 and you can give
    it as a webhook req.

     */

    @GetMapping("/{id}")
    public PaymentStatus checkPaymentStatus(@PathVariable("id") String PaymentGatewayPaymentId){
         return this.paymentService.getPaymentStatus(PaymentGatewayPaymentId);
    }
}
