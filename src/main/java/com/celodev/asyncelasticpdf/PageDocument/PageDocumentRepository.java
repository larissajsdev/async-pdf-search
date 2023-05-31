package com.celodev.asyncelasticpdf.PageDocument;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface PageDocumentRepository extends ReactiveElasticsearchRepository<PageDocument, String> {

  @Query("{\"match_phrase\": {\"content\": \"?0\"}}")
  @Highlight(fields = {@HighlightField(name = "content")})
  Flux<SearchHit<PageDocument>> findByContentPhraseQuery(String query, Pageable pageable);

  @Highlight(fields = {@HighlightField(name = "content")})
  Flux<SearchHit<PageDocument>> findByContent(String query, Pageable pageable);

}
