package com.easymedia;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;


/**
 * @ClassName : EgovConfigAppCommon.java
 * @Description : 공통 Bean 설정
 *
 * @author : 윤주호
 * @since  : 2021. 7. 20
 * @version : 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일              수정자               수정내용
 *  -------------  ------------   ---------------------
 *   2021. 7. 20    윤주호               최초 생성
 * </pre>
 *
 */
@Configuration
@ComponentScan(basePackages = "org.egovframe", includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Repository.class)
}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Configuration.class)
})
public class EgovMessageConfig {

    /**
     * @return [Resource 설정] 메세지 Properties 경로 설정
     */
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasenames(
                "classpath:/egovframework/message/com/message-common",
                "classpath:/org/egovframe/rte/fdl/idgnr/messages/idgnr",
                "classpath:/org/egovframe/rte/fdl/property/messages/properties");
        reloadableResourceBundleMessageSource.setCacheSeconds(60);
        return reloadableResourceBundleMessageSource;
    }

    /**
     * @return [Resource 설정] 메세지 소스 등록
     */
    @Bean
    public EgovMessageSource egovMessageSource() {
        EgovMessageSource egovMessageSource = new EgovMessageSource();
        egovMessageSource.setReloadableResourceBundleMessageSource(messageSource());
        return egovMessageSource;
    }
}
