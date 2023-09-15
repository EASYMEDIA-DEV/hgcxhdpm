package com.easymedia.api.config;

import com.easymedia.api.resolver.ApiDataHandlerMethodArgumentResolver;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <pre>
 * Data config 설정
 * </pre>
 *
 * @ClassName		    : ApiWebMvcConfig.java
 * @Description		: XSS 필터 config 설정
 * @author 박주석
 * @since 2022.01.10
 * @version 1.0
 * @see
 * @Modification Information
 * <pre>
 * 		 since		  author	            description
 *    ==========    ==========    ==============================
 *    2022.01.10	  박주석	             최초 생성
 * </pre>
 */
@Configuration
@RequiredArgsConstructor
public class ApiWebMvcConfig implements WebMvcConfigurer {

    private final ApiDataHandlerMethodArgumentResolver dataHandlerMethodArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(dataHandlerMethodArgumentResolver);
    }

    //XSS 처리
    @Bean
    public FilterRegistrationBean<XssEscapeServletFilter> getFilterRegistrationBean()
    {
        FilterRegistrationBean<XssEscapeServletFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssEscapeServletFilter());
        registrationBean.setOrder(1);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://localhost:3000", "https://127.0.0.1:3000")
                .allowedMethods("GET", "POST", "DELETE", "PUT") // 허용할 HTTP method
                .allowCredentials(true) // 쿠키 인증 요청 허용
                .exposedHeaders("Authorization_mngwserctemp", "Authorization_mngwserc")
                .maxAge(3000); // 원하는 시간만큼 pre-flight 리퀘스트를 캐싱
    }

}
