package com.agentus.sse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionSSE {
    private UUID id;
    private FluxSink<ServerSentEvent<?>> fluxSink;
}
