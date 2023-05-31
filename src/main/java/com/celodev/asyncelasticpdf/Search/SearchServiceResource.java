package com.celodev.asyncelasticpdf.Search;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/search")
public class SearchServiceResource {

    private final SearchServiceImpl service;

    @GetMapping
    public Flux<SearchResponse> findAll(SearchRequest request) {
        return service.searchPageDocuments(request);
    }
}
