package com.abc.test.cabapp.cache;

import java.util.Arrays;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfigurationSimple {
	@Bean
	public KeyGenerator buildTripReportKeyGenerator() {
		return new CustomTriReportKeyGenerator();
	}

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        Cache reportCache= new ConcurrentMapCache("trip-report");
        Cache reportCacheMultiMedallion= new ConcurrentMapCache("trip-reports");
        cacheManager.setCaches(Arrays.asList(reportCache, reportCacheMultiMedallion));
        return cacheManager;
    }
}
