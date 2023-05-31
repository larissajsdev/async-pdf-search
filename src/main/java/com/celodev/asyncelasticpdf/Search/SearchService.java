package com.celodev.asyncelasticpdf.Search;

import reactor.core.publisher.Flux;

public interface SearchService {
    Flux<SearchResponse> findByContentWithHighlight(SearchRequest request);

    Flux<SearchResponse> findByContentPhraseQueryHighlight(SearchRequest request);
}
