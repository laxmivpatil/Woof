package com.example.techverse.service;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayException;
import com.razorpay.RazorpayClient;
 
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class OrderService1 {

     
    private String keyId="rzp_test_wIkA36LzCEN5sz";
 
    private String keySecret="q66erZ3vUMDufjJyZi3iA7Qy";

    public String createOrder(double amount, String currency, String receipt) throws RazorpayException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, keySecret);
        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // Razorpay expects amount in paisa, so multiply by 100 for rupees
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);
        //orderRequest.put("payment_capture", payment_capture); // 1 for automatic capture, 0 for manual capture

        Order order = razorpayClient.orders.create(orderRequest);
        return order.get("id");
    }
}
