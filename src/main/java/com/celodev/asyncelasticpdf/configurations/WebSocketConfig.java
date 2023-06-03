package com.celodev.asyncelasticpdf.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class WebSocketConfig {

  @Bean
  public ChatMessageProcessor chatMessageProcessor() {
    return new ChatMessageProcessor();
  }

  @Bean
  public WebSocketHandler simpleChatWebSocketHandler(ChatMessageProcessor chatMessageProcessor) {
    Flux<String> messageFlux = chatMessageProcessor.getMessageFlux();
    return new SimpleChatWebSocketHandler(messageFlux);
  }

  @Bean
  public HandlerMapping webSocketHandlerMapping(WebSocketHandler simpleChatWebSocketHandler) {
    Map<String, WebSocketHandler> map = new HashMap<>();
    map.put("/chat", simpleChatWebSocketHandler);

    SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
    handlerMapping.setOrder(1);
    handlerMapping.setUrlMap(map);

    return handlerMapping;
  }

  @Bean
  public WebSocketHandlerAdapter webSocketHandlerAdapter() {
    return new WebSocketHandlerAdapter();
  }
}
