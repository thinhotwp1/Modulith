package com.example.warehouse.inventory;

import com.example.warehouse.common.events.GoodsReceivedEvent;
import com.example.warehouse.common.events.OrderShippingEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final Map<String, Integer> inventory = new ConcurrentHashMap<>();

    private final ApplicationEventPublisher events;

    @EventListener
    void on(GoodsReceivedEvent event) {
        inventory.merge(event.sku(), event.qty(), Integer::sum);
        System.out.println("Updated inventory: " + event.sku() + " = " + inventory.get(event.sku()));
    }

    public void order(String sku, int qty) {
        if (inventory.get(sku) == null || inventory.get(sku) < qty) {
            System.out.println("Not enough inventory");
            return;
        }

        inventory.put(sku, inventory.get(sku) - qty);
        shippingOrder(sku, qty);
    }

    private void shippingOrder(String sku, int qty) {
        events.publishEvent(new OrderShippingEvent(sku, qty));
    }
}
