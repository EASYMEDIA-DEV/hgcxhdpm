package com.easymedia.property;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ToString
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("file")
@Slf4j
public class FileUploadProperties {

    private final String uploadPath;
    private final String imageExtns;
    private final String videoExtns;
    private final String fileExtns;
    private final Long maxSize;



}
