package com.celodev.asyncelasticpdf.pdf;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface PdfDocumentRepository extends ReactiveElasticsearchRepository<PdfDocument, String> {

  @Highlight(fields = {@HighlightField(name = "pageDocuments.content")})
  Flux<SearchHit<PdfDocument>> findByPageDocumentsContent(String query, Pageable pageable);

  @Query("{\"match_phrase\": {\"pageDocuments.content\": \"?0\"}}")
  @Highlight(fields = {@HighlightField(name = "pageDocuments.content")})
  Flux<SearchHit<PdfDocument>> findByContentPhraseQuery(String query, Pageable pageable);

}
