package com.example.warehouse.application;

import com.example.warehouse.receiving.ReceivingService;
import lombok.RequiredArgsConstructor;
import org.springframework.modulith.ApplicationModule;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/receiving")
@ApplicationModule
public class ReceivingController {

    private final ReceivingService service;

    @PostMapping
    public void receive(@RequestParam String sku, @RequestParam int qty) {
        service.receiveGoods(sku, qty);
    }
}
