package com.example.warehouse.order;

import com.example.warehouse.common.events.OrderMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class OrderService {

    private final ApplicationEventPublisher events;

    public void order(String sku, int qty) {
        events.publishEvent(new OrderMessageEvent(sku, qty));
    }
}
