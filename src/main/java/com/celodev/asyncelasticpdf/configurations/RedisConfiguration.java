package com.celodev.asyncelasticpdf.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.celodev.asyncelasticpdf.Search.SearchResponse;

@Configuration
public class RedisConfiguration {

  @Bean
  LettuceConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory();
  }

  @Bean
  ReactiveRedisTemplate<String, SearchResponse> reactiveRedisTemplate(
      ReactiveRedisConnectionFactory redisConnectionFactory) {
    RedisSerializer<String> keySerializer = new StringRedisSerializer();
    RedisSerializationContext<String, SearchResponse> serializationContext = RedisSerializationContext
        .<String, SearchResponse>newSerializationContext(keySerializer)
        .value(new Jackson2JsonRedisSerializer<>(SearchResponse.class))
        .build();
    return new ReactiveRedisTemplate<>(redisConnectionFactory, serializationContext);
  }
}