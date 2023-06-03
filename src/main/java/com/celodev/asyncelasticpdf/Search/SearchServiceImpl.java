package com.celodev.asyncelasticpdf.Search;

import com.celodev.asyncelasticpdf.Cache.RedisCacheServiceImpl;
import com.celodev.asyncelasticpdf.PageDocument.PageDocument;
import com.celodev.asyncelasticpdf.PageDocument.PageDocumentRepository;
import com.celodev.asyncelasticpdf.PageDocument.PageDocumentService;
import com.celodev.asyncelasticpdf.exceptions.SearchException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {

  private final PageDocumentRepository reactiveElasticRepository;
  private final RedisCacheServiceImpl cacheService;

  Logger logger = LoggerFactory.getLogger(PageDocumentService.class);

  public Flux<SearchResponse> searchPageDocuments(SearchRequest request) {
    String cacheKey = cacheService.createCacheKey(request);

    logger.info("cache-key-redis: " + cacheKey);

    return cacheService
      .getDocumentsFromCache(cacheKey)
      .switchIfEmpty(findByContentWithHighlightBasedOnQueryType(request)
        .flatMap(searchResponse -> cacheService.saveDocumentToCache(cacheKey, searchResponse)))
      .onErrorResume(throwable -> Mono.error(
        new SearchException("An error occurred while searching for documents: " + throwable.getMessage())));
  }

  @Override
  public Flux<SearchResponse> findByContentWithHighlight(SearchRequest request) {
    return toSearchResponseFlux(reactiveElasticRepository
      .findByContent(request.getQuery(), PageRequest.of(request.getPage(), request.getSize())));
  }

  @Override
  public Flux<SearchResponse> findByContentPhraseQueryHighlight(SearchRequest request) {
    return toSearchResponseFlux(reactiveElasticRepository
      .findByContentPhraseQuery(request.getQuery(), PageRequest.of(request.getPage(), request.getSize()))).log();
  }

  private Flux<SearchResponse> toSearchResponseFlux(Flux<SearchHit<PageDocument>> hitsFlux) {
    return hitsFlux.map(SearchResponse::new);
  }

  private Flux<SearchResponse> findByContentWithHighlightBasedOnQueryType(SearchRequest request) {
    return isPhraseQuery(request.getQuery())
      .flatMapMany(isPhrase -> isPhrase
        ? findByContentPhraseQueryHighlight(request)
        : findByContentWithHighlight(request)
      ).log();
  }

  private Mono<Boolean> isPhraseQuery(String query) {
    return Mono.fromSupplier(() -> query.contains(" "));
  }
}
