package com.cvgenie.backend.controller;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @PostMapping("/create-order")
    public ResponseEntity<Object> createOrder(@RequestBody Map<String, Object> data) {
        try {
            int amount = (int) data.get("amount");

            RazorpayClient razorpay = new RazorpayClient("rzp_test_WSjGnudHEqRR0n", "UPLCCANy6cegQJoKsGU2204O");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

            Order order = razorpay.orders.create(orderRequest);

            return ResponseEntity.ok(order.toJson().toMap()); // 👈 Proper JSON
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}