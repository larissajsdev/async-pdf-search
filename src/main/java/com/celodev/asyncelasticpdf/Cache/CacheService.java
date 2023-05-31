package com.celodev.asyncelasticpdf.Cache;

import com.celodev.asyncelasticpdf.Search.SearchRequest;
import com.celodev.asyncelasticpdf.Search.SearchResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CacheService {
    String createCacheKey(SearchRequest request);

    Mono<SearchResponse> saveDocumentToCache(String cacheKey, SearchResponse searchResponse);

    Flux<SearchResponse> getDocumentsFromCache(String cacheKey);
}
