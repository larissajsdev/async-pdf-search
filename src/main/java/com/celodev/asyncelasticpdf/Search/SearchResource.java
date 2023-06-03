package com.celodev.asyncelasticpdf.Search;

import com.celodev.asyncelasticpdf.configurations.ChatMessageProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchResource {

  private final SearchServiceImpl service;
  private final ChatMessageProcessor chatMessageProcessor;

  @GetMapping
  public Flux<SearchResponse> findAll(SearchRequest request) {
    return service.searchPageDocuments(request)
      .doOnNext(d -> chatMessageProcessor.broadcastMessage(d.getId()));
  }
}
