package com.easymedia.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * <pre>
 * API swagger 처리
 * </pre>
 *
 * @ClassName		    : ApiSwaggerConfig.java
 * @Description		: API swagger 처리
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
@Configuration
public class ApiSwaggerConfig {

    /**
     * openAPI
     * @return
     */
    @Bean
    public OpenAPI openAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API 명세서")
                        .version("V1.0")
                        .contact(new Contact().name("이지미디어"))
                );
    }

    @Bean
    public GroupedOpenApi openAPIVLogin() {
        return GroupedOpenApi.builder()
                .group("version 1")
                .pathsToMatch("/api/v1/**")    // 처리될 url
                .packagesToScan("com.easymedia.api.web")
                //.pathsToExclude("/login")   // 제외 url
                .build();
    }
}
