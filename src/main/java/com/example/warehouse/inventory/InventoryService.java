package com.example.warehouse.inventory;

import com.example.warehouse.common.events.InventoryShippingMessageEvent;
import com.example.warehouse.common.events.ReceivedMessageEvent;
import com.example.warehouse.common.events.OrderMessageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@RequiredArgsConstructor
public class InventoryService {

    private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher events;

    @EventListener
    void storeItem(ReceivedMessageEvent event) {
        inventory.merge(event.sku(), event.qty(), Integer::sum);
        updateStorage(event.sku());
    }

    private void updateStorage(String sku) {
        log.info("Updated inventory: {} = {}", sku, inventory.get(sku));
    }

    @EventListener
    void checkInventoryOrder(OrderMessageEvent event) {
        if (inventory.get(event.sku()) == null || inventory.get(event.sku()) < event.qty()) {
            log.info("Not enough inventory for SKU {}, requested {}, actually {}", event.sku(), event.qty(), inventory.get(event.sku()));
            return;
        }

        inventory.put(event.sku(), inventory.get(event.sku()) - event.qty());
        updateStorage(event.sku());

        shippingOrder(event.sku(), event.qty());
    }

    private void shippingOrder(String sku, int qty) {
        events.publishEvent(new InventoryShippingMessageEvent(sku, qty));
    }
}
