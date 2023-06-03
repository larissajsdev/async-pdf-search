package com.celodev.asyncelasticpdf.configurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleChatWebSocketHandler implements WebSocketHandler {

  private Flux<String> messageFlux;
  private ChatMessageProcessor chatMessageProcessor;

  public SimpleChatWebSocketHandler(Flux<String> messageFlux) {
    this.messageFlux = messageFlux;
  }

  @Override
  public @NotNull Mono<Void> handle(WebSocketSession session) {
    Flux<WebSocketMessage> output = chatMessageProcessor
      .getMessageFlux().map(session::textMessage);


    Mono<Void> input = session.receive()
      .map(WebSocketMessage::getPayloadAsText)
      .doOnNext(chatMessageProcessor::broadcastMessage)
      .then();

    return session.send(output).and(input);
  }
}
