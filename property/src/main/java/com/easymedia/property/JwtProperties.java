package com.easymedia.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ToString
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("jwt")
public class JwtProperties {
    private final String secretKey;
    private final String tokenHeader;
    private final String tokenPrefix;
    private final String tokenType;
    private final String authLoginUrl;
    private final long expiration;
    private final Integer cookieMaxAge;
}
