package com.celodev.asyncelasticpdf.configurations;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

public class ChatMessageProcessor {


  private final Sinks.Many<String> sink;
  private final Flux<String> messageFlux;

  public ChatMessageProcessor() {
    this.sink = Sinks.many().multicast().onBackpressureBuffer();
    this.messageFlux = sink.asFlux();
  }

  public void broadcastMessage(String message) {
    sink.tryEmitNext(message);
  }

  public Flux<String> getMessageFlux() {
    return messageFlux;
  }
}
