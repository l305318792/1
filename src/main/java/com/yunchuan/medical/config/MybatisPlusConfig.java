package com.yunchuan.medical.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;

/**
 * MyBatis-Plus 配置类
 */
@Configuration
@MapperScan("com.yunchuan.medical.mapper")
public class MybatisPlusConfig {
    
    private static final Logger log = LoggerFactory.getLogger(MybatisPlusConfig.class);

    /**
     * 配置SqlSessionFactory
     */
    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws Exception {
        log.info("初始化 MyBatis SqlSessionFactory");
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/*.xml"));
        sessionFactory.setTypeAliasesPackage("com.yunchuan.medical.entity");
        log.info("MyBatis SqlSessionFactory 配置完成");
        return sessionFactory;
    }

    /**
     * 配置分页插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        log.info("初始化 MyBatis-Plus 分页插件");
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        
        // 创建分页插件并设置数据库类型为 MySQL
        PaginationInnerInterceptor paginationInterceptor = new PaginationInnerInterceptor();
        // 设置数据库类型
        paginationInterceptor.setDbType(DbType.MYSQL);
        // 设置最大单页限制数量，默认 500 条，-1 不受限制
        paginationInterceptor.setMaxLimit(500L);
        // 设置请求的页面大于最大页后操作，true调回到首页，false继续请求
        paginationInterceptor.setOverflow(false);
        // 设置count查询优化
        paginationInterceptor.setOptimizeJoin(true);
        
        interceptor.addInnerInterceptor(paginationInterceptor);
        log.info("MyBatis-Plus 分页插件配置完成");
        return interceptor;
    }
} 