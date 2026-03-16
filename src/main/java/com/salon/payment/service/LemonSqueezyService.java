package com.salon.payment.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salon.payment.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LemonSqueezyService {

    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${lemon.squeezy.api-key}")
    private String apiKey;

    @Value("${lemon.squeezy.store-id}")
    private String storeId;

    @Value("${lemon.squeezy.checkout-url}")
    private String checkoutUrl;

    public Mono<CheckoutResponse> createCheckout(Payment payment, String successUrl) {
        WebClient webClient = webClientBuilder.build();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("checkout_data", Map.of(
                "email", payment.getCustomerEmail(),
                "name", payment.getCustomerName(),
                "custom", Map.of(
                        "booking_id", payment.getBookingId(),
                        "payment_id", payment.getId()
                )
        ));
        attributes.put("product_options", Map.of(
                "redirect_url", successUrl,
                "enabled_variants", new String[]{payment.getLemonSqueezyVariantId()}
        ));

        Map<String, Object> relationships = new HashMap<>();
        relationships.put("store", Map.of(
                "data", Map.of(
                        "type", "stores",
                        "id", storeId
                )
        ));
        relationships.put("variant", Map.of(
                "data", Map.of(
                        "type", "variants",
                        "id", payment.getLemonSqueezyVariantId()
                )
        ));

        Map<String, Object> data = new HashMap<>();
        data.put("type", "checkouts");
        data.put("attributes", attributes);
        data.put("relationships", relationships);

        Map<String, Object> requestBody = Map.of("data", data);

        log.info("Sending checkout request to Lemon Squeezy: {}", requestBody);

        return webClient.post()
                .uri(checkoutUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .header(HttpHeaders.ACCEPT, "application/vnd.api+json")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.api+json")
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("Lemon Squeezy API error: {}", errorBody);
                                    return Mono.error(new RuntimeException("Lemon Squeezy API error: " + errorBody));
                                })
                )
                .bodyToMono(Map.class)
                .map(response -> {
                    log.info("Lemon Squeezy response: {}", response);

                    try {
                        // Convert response to JsonNode for easier parsing
                        JsonNode root = objectMapper.valueToTree(response);

                        // Extract checkout ID and URL
                        JsonNode dataNode = root.path("data");
                        String checkoutRequestId = dataNode.path("id").asText();

                        JsonNode attributesNode = dataNode.path("attributes");
                        String url = attributesNode.path("url").asText();

                        if (url == null || url.isEmpty()) {
                            throw new RuntimeException("Invalid Lemon Squeezy response: missing checkout url");
                        }

                        log.info("Checkout created - ID: {}, URL: {}", checkoutRequestId, url);

                        return new CheckoutResponse(checkoutRequestId, url);

                    } catch (Exception e) {
                        log.error("Error parsing Lemon Squeezy response", e);
                        throw new RuntimeException("Failed to parse Lemon Squeezy response", e);
                    }
                });
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    public static class CheckoutResponse {
        private String checkoutRequestId;
        private String url;
    }
}