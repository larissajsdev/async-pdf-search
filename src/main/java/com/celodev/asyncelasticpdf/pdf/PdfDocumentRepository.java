package com.celodev.asyncelasticpdf.pdf;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ReactiveElasticsearchRepository;
import reactor.core.publisher.Flux;

public interface PdfDocumentRepository extends ReactiveElasticsearchRepository<PdfDocument, String> {
//
//  @Query("{\"match_phrase\": {\"content\": \"?0\"}}")
//  @Highlight(fields = {@HighlightField(name = "content")})
//  Flux<SearchHit<PageDocument>> findByContentPhraseQuery(String query, Pageable pageable);

    @Highlight(fields = {@HighlightField(name = "pageDocuments.content")})
    Flux<SearchHit<PdfDocument>> findByPageDocumentsContent(String query, Pageable pageable);


//  @Highlight(fields = {@HighlightField(name = "content")})
//  Flux<SearchHit<PageDocument>> findByContentContaining(String phrase, Pageable pageable);
}
