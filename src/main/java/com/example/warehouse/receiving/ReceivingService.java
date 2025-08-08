package com.example.warehouse.receiving;

import com.example.warehouse.common.events.GoodsReceivedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReceivingService {

    private final ApplicationEventPublisher events;

    public void receiveGoods(String sku, int qty) {
        System.out.println("Receiving goods for sku: " + sku + ", qty: " + qty);
        events.publishEvent(new GoodsReceivedEvent(sku, qty));
    }
}
