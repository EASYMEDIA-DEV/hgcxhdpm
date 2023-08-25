package com.easymedia.api.config;

import com.easymedia.api.jwt.JwtRequestFilter;
import com.easymedia.enums.menu.Site;
import com.easymedia.jwt.JwtTokenProvider;
import com.easymedia.property.JwtProperties;
import com.easymedia.service.ApiUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <pre>
 * API 보안 처리
 * </pre>
 *
 * @ClassName		    : ApiSecurityConfig.java
 * @Description		: API 보안 처리
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
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApiSecurityConfig {

    private final JwtProperties _jwtProperties;
    private final JwtTokenProvider _apiJwtTokenProvider;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers( "/*.*",
                        "/error",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/swagger**",
                        "/files/**",
                        "/api-docs/**",
                        "/v3/api-docs",
                        "/api/v1/sample/**",
                        "/api/v1/setLogin",
                        "/api/v1/setLogout")
                .mvcMatchers("/*.*",
                        "/error",
                        "/files/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/swagger**",
                        "/api-docs/**",
                        "/v3/api-docs",
                        "/api/v1/sample/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //CSRF, HEADER
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        //http.csrf().ignoringAntMatchers("/api/v1/login", "/api/v1/logout").csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        //       .and().exceptionHandling().accessDeniedHandler(new ApiAccessDeniedHandler());
        http.requestCache().disable().csrf().disable();
        http.headers().frameOptions().disable();
        http.cors();
        //인증
        http
                .authorizeRequests()
                .mvcMatchers("/api/v1/setLogin", "/api/v1/setLogout").permitAll()
                .antMatchers("/api/v1/setLogin", "/api/v1/setLogout").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(new JwtRequestFilter(_jwtProperties, _apiJwtTokenProvider, Site.MNGWSERC), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
