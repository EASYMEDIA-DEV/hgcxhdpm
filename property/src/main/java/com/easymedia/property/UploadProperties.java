package com.easymedia.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.stereotype.Component;

@Getter
@ToString
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("upload")
public class UploadProperties {
    private final String company;
    private final String notice;
    private final String promotion;
    private final String product;
    private final String banner;
    private final String mainConfig;
    private final String faq;
    private final String booking;
}
