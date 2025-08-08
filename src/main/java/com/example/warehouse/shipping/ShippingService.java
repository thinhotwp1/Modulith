package com.example.warehouse.shipping;

import com.example.warehouse.common.events.InventoryShippingMessageEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ShippingService {

    @EventListener()
    public void shipOrder(InventoryShippingMessageEvent event) {
        log.info("Shipping {} of {}", event.qty(), event.sku());
    }
}
