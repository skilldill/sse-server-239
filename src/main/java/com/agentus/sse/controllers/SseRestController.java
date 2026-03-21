package com.agentus.sse.controllers;

import com.agentus.sse.SubscriptionSSE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
public class SseRestController {
    Map<UUID, SubscriptionSSE> subscriptions = new ConcurrentHashMap<>();

    @GetMapping(path = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<?>> openSSeStream() {

        return Flux.create(fluxSink -> {
            UUID id = UUID.randomUUID();
            log.info("creating subscription for{}", id);
            fluxSink.onCancel(
                    () -> {
                        subscriptions.remove(id);
                        log.info("subscription {} was closed", id);
                    }

            );

            SubscriptionSSE subscriptionSSE = new SubscriptionSSE(id, fluxSink);
            subscriptions.put(id, subscriptionSSE);

            ServerSentEvent<String> responseEvent = ServerSentEvent.builder(String.format("CLIENT %s CONNECTED", id)).build();
            fluxSink.next(responseEvent);
        });
    }

    @PostMapping("/trigger-message")
    public void triggerMessageAll() {
        String payload = "Triggered by HTTP at " + Instant.now();
        ServerSentEvent<String> event = ServerSentEvent.builder(payload).event("server-message").build();
        subscriptions.values().forEach(s -> {
            try { s.getFluxSink().next(event); }
            catch (Exception e) { log.warn("send failed {}", s.getId()); }
        });
    }
}
