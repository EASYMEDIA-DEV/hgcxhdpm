package com.easymedia;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * <pre>
 * 마이바티스 Config 설정
 * </pre>
 *
 * @ClassName		    : MybatisConfig.java
 * @Description		: 마이바티스 Config 설정
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
//JAVA 파일 SCAN 위치
@MapperScan(basePackages= {"com.easymedia.service.dao"}, sqlSessionFactoryRef = "SqlSessionFactory", sqlSessionTemplateRef = "SessionTemplate")
//트랜젝션 사용
@EnableTransactionManagement
public class MybatisConfig {
    //매퍼 xml 위치
    @Value("classpath:mappers/*.xml")
    private String mPath;
    //마이바티스 config xml 위치
    @Value("classpath:mybatis.xml")
    private String cPath;

    @Value("${spring.db.datasource.jndi-name}")
    private String jndiName;

    //DATABASE
    @Bean(name = "dataSource")
    @Profile("dev_hgcx")
    @ConfigurationProperties(prefix = "spring.hcsdb.datasource")
    public DataSource DataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * dataSource 생성
     * @return
     */
    //DATABASE
    @Bean(name = "dataSource")
    @Profile("!dev_hgcx")
    public DataSource jndiDataSource() {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        DataSource dataSource = dataSourceLookup.getDataSource(jndiName);
        return dataSource;
    }

    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory SqlSessionFactory(@Qualifier("dataSource") DataSource DataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(DataSource);
        sqlSessionFactoryBean.setConfigLocation(applicationContext.getResources(cPath)[0]);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mPath));
        sqlSessionFactoryBean.setTypeAliasesPackage("com.easymedia.dto");
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "SessionTemplate")
    public SqlSessionTemplate SqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory firstSqlSessionFactory) {
        return new SqlSessionTemplate(firstSqlSessionFactory);
    }
}