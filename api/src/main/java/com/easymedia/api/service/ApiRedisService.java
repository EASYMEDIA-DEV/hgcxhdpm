package com.easymedia.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * <pre>
 * 사용자(apikey) 조회 체크
 * </pre>
 *
 * @ClassName		    : ApiUserDetailsService.java
 * @Description		: 사용자(apikey) 조회 체크
 * @author 박주석
 * @since 2023.01.20
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2023.01.20	  박주석	             최초 생성
 * </pre>
 */
@Slf4j
@Service
public class ApiRedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public ApiRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setData(String key, String value,Long expiredTime){
        redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.MILLISECONDS);
    }

    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }
}