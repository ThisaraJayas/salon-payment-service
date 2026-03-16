package com.salon.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.Map;

@Data
public class LemonSqueezyWebhookPayload {
    private String eventName;
    private DataObject data;

    @lombok.Data
    public static class DataObject {
        private String id;
        private String type;
        private Attributes attributes;
        private Relationships relationships;
        private Map<String, Object> meta;
    }

    @lombok.Data
    public static class Attributes {
        private String status;
        private String email;
        private String name;
        @JsonProperty("total")
        private Double total;
        private String currency;
        @JsonProperty("variant_id")
        private String variantId;
        @JsonProperty("product_id")
        private String productId;
        @JsonProperty("order_id")
        private String orderId;
        @JsonProperty("created_at")
        private String createdAt;
        @JsonProperty("updated_at")
        private String updatedAt;
        @JsonProperty("checkout_id")
        private String checkoutId;
        @JsonProperty("custom_data")
        private Map<String, Object> customData;
    }

    @lombok.Data
    public static class Relationships {
        private Order order;
        private Variant variant;
        private Product product;
    }

    @lombok.Data
    public static class Order {
        private DataObject data;
    }

    @lombok.Data
    public static class Variant {
        private DataObject data;
    }

    @lombok.Data
    public static class Product {
        private DataObject data;
    }
}