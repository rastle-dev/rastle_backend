package rastle.dev.rastle_backend.global.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;
@Configuration
@EnableJpaRepositories( basePackages =  {DatabaseConfig.RDS_DOMAIN_REPO})
@MapperScan(
        value = {DatabaseConfig.RDS_DOMAIN_MAPPER}
)
@RequiredArgsConstructor
@Slf4j
public class DatabaseConfig {
    static final String RDS_DOMAIN_REPO = "rastle.dev.rastle_backend.domain.*.repository";
    static final String RDS_DOMAIN_MAPPER = "rastle.dev.rastle_backend.domain.*.mapper";


    private final DatabaseProperty databaseProperty;

    public DataSource routingDataProperty(String url){
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(databaseProperty.getUrl());
        hikariDataSource.setDriverClassName(databaseProperty.getDriverClassName());
        hikariDataSource.setPassword(databaseProperty.getPassword());
        hikariDataSource.setUsername(databaseProperty.getUsername());

        return hikariDataSource;
    }


    @Bean
    public DataSource routingDataSource(){
        ReplicationRoutingDataSource replicationRoutingDataSource = new ReplicationRoutingDataSource();
        DataSource master = routingDataProperty(databaseProperty.getUrl());


        Map<Object,Object> dataSourceMap = new LinkedHashMap<>();
        dataSourceMap.put("master",master);

        databaseProperty.getSlaveList().forEach(slave -> {
            dataSourceMap.put(slave.getName() , routingDataProperty(slave.getUrl()));
        });

        replicationRoutingDataSource.setTargetDataSources(dataSourceMap);
        replicationRoutingDataSource.setDefaultTargetDataSource(master);
        return replicationRoutingDataSource;
    }

    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(){
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource());
        entityManagerFactoryBean.setPackagesToScan("rastle.dev.rastle_backend");
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);


        return entityManagerFactoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:/mapper/*.xml"));
        return sessionFactory.getObject();
    }
}
