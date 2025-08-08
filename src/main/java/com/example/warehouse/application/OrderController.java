package com.example.warehouse.application;

import com.example.warehouse.inventory.InventoryService;
import com.example.warehouse.receiving.ReceivingService;
import com.example.warehouse.shipping.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final InventoryService inventoryService;

    @PostMapping
    public void order(@RequestParam String sku, @RequestParam int qty) {
        inventoryService.order(sku, qty);
    }
}
