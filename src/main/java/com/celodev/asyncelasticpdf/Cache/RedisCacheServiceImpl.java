package com.celodev.asyncelasticpdf.Cache;

import com.celodev.asyncelasticpdf.PageDocument.PageDocumentService;
import com.celodev.asyncelasticpdf.Search.SearchRequest;
import com.celodev.asyncelasticpdf.Search.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisCacheServiceImpl implements CacheService {

    private final ReactiveRedisTemplate<String, SearchResponse> reactiveRedisTemplate;

    Logger logger = LoggerFactory.getLogger(PageDocumentService.class);

    @Value("${spring.data.redis.expiration.cache.minutes}")
    int EXPIRATION_CACHE;

    @Override
    public String createCacheKey(SearchRequest request) {
        logger.info("creating cache key for:" + request.toString());
        return "search:" + request.getQuery() + ":page:" + request.getPage() + ":size:" + request.getSize();

    }

    public Flux<SearchResponse> getDocumentsFromCache(String cacheKey) {
        return reactiveRedisTemplate.opsForSet()
                .members(cacheKey)
                .collectList()
                .flatMapIterable(d -> d)
                .log();
    }

    public Mono<SearchResponse> saveDocumentToCache(String cacheKey, SearchResponse elasticDocument) {
        return reactiveRedisTemplate.opsForSet()
                .add(cacheKey, elasticDocument)
                .then(reactiveRedisTemplate.expire(cacheKey, Duration.ofMinutes(EXPIRATION_CACHE)))
                .then(Mono.just(elasticDocument))
                .log();
    }
}
