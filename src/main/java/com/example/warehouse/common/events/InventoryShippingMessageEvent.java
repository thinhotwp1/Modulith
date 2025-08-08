package com.example.warehouse.common.events;

public record InventoryShippingMessageEvent(String sku, int qty) {
}
