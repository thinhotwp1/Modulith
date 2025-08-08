package com.example.warehouse.receiving;

import com.example.warehouse.common.events.ReceivedMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class ReceivingService {

    private final ApplicationEventPublisher events;

    public void receiveGoods(String sku, int qty) {
        log.info("Receiving goods for sku: {}, qty: {}", sku, qty);
        events.publishEvent(new ReceivedMessageEvent(sku, qty));
    }
}
