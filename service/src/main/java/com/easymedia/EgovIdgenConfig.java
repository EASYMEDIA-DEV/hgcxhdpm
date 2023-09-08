package com.easymedia;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.egovframe.rte.fdl.idgnr.impl.EgovSequenceIdGnrServiceImpl;
import org.egovframe.rte.fdl.idgnr.impl.EgovTableIdGnrServiceImpl;
import org.egovframe.rte.fdl.idgnr.impl.strategy.EgovIdGnrStrategyImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class EgovIdgenConfig {

    @Autowired
    @Qualifier("dataSource")
    DataSource dataSource;

    /**
     * 첨부파일 ID Generation  Strategy Config
     * @return
     */
    private EgovIdGnrStrategyImpl fileStrategy() {
        EgovIdGnrStrategyImpl egovIdGnrStrategyImpl = new EgovIdGnrStrategyImpl();
        egovIdGnrStrategyImpl.setPrefix("FILE_");
        egovIdGnrStrategyImpl.setCipers(15);
        egovIdGnrStrategyImpl.setFillChar('0');
        return egovIdGnrStrategyImpl;
    }

    /**
     * 첨부파일 ID Generation  Config
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl egovFileIdGnrService() {
        EgovTableIdGnrServiceImpl egovTableIdGnrServiceImpl = new EgovTableIdGnrServiceImpl();
        egovTableIdGnrServiceImpl.setDataSource(dataSource);
        egovTableIdGnrServiceImpl.setStrategy(fileStrategy());
        egovTableIdGnrServiceImpl.setBlockSize(1);
        egovTableIdGnrServiceImpl.setTable("CO_SEQ_MST");
        egovTableIdGnrServiceImpl.setTableName("FILE_ID");
        return egovTableIdGnrServiceImpl;
    }

    /** 샘플 테이블 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl sampleTableIdGnrService() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setTableName("SAMPLE_ID")
                .setCipers(13)
                .build();
    }

    /** 메뉴 테이블 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl menuTableIdGnrService() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setTableName("MENU_TEST_SEQ")
                .setCipers(13)
                .build();
    }

    /** 샘플 쿼리 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovSequenceIdGnrServiceImpl sampleQueryIdGnrService() {
        EgovSequenceIdGnrServiceImpl egovIdGnrStrategyImpl = new EgovSequenceIdGnrServiceImpl();
        egovIdGnrStrategyImpl.setDataSource(dataSource);
        egovIdGnrStrategyImpl.setQuery("SELECT SEQ_SAMPLE.NEXTVAL FROM DUAL");
        return egovIdGnrStrategyImpl;
    }


    /** 시스템 로그 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl serviceLogIdGnrService() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setPreFix("SYSLOG_")
                .setTableName("SYSLOG_ID")
                .setCipers(13)
                .build();
    }

    /** 관리자 계정 테이블 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl admIdgen() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setTableName("ADM_SEQ")
                .setCipers(13)
                .build();
    }

    /** 관리자 알림 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl admNtcIdgen() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setTableName("ADM_NTC_SEQ")
                .setCipers(13)
                .build();
    }

    /** 관리자 알림 시퀀스
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl egovSysLogIdGnrService() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setPreFix("SYSLOG_")
                .setTable("CO_SEQ_MST")
                .setTableName("SYSLOG_ID")
                .setCipers(13)
                .build();
    }

    /** 메뉴
     * @return
     */
    @Bean(destroyMethod = "destroy")
    public EgovTableIdGnrServiceImpl menuIdgen() {
        return new EgovIdGnrBuilder().setDataSource(dataSource).setEgovIdGnrStrategyImpl(new EgovIdGnrStrategyImpl())
                .setBlockSize(1)
                .setTable("CO_SEQ_MST")
                .setTableName("MENU_SEQ")
                .setCipers(13)
                .build();
    }
}
