package com.easymedia.service;

import com.easymedia.error.exception.NotAllowedClientSiteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;


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
@Validated
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApiUserDetailsService implements UserDetailsService {

    /**
     * API 로그인 스위치
     * @param apiKey
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String apiKey) throws UsernameNotFoundException {
        Assert.hasText(apiKey, "회사정보가 없습니다.");
        throw new NotAllowedClientSiteException(apiKey);
    }
}