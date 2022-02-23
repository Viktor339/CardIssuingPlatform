package com.cardissuingplatform.config;


import com.google.common.cache.CacheBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "cache.enabled"
)
public class CacheConfig implements CachingConfigurer {

    private final CacheProperties cacheProperties;

    @Override
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager("user") {

            @Override
            protected Cache createConcurrentMapCache(final String name) {
                return new ConcurrentMapCache(name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(cacheProperties.getTimeToLive())
                                .maximumSize(cacheProperties.getMaxSize())
                                .build()
                                .asMap(), false);
            }
        };
    }

}
