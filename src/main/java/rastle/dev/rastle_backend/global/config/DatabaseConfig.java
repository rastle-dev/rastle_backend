//package rastle.dev.rastle_backend.global.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import rastle.dev.rastle_backend.global.common.enums.DataSourceType;
//import rastle.dev.rastle_backend.global.config.ReplicationDataSourceProperty.Slave;
//
//import javax.sql.DataSource;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
//@Configuration
//@EnableJpaAuditing
//@RequiredArgsConstructor
//public class DatabaseConfig {
//    static final String RDS_DOMAIN_REPO = "rastle.dev.rastle_backend.domain.*.repository.mysql";
//    static final String RDS_DOMAIN_MAPPER = "rastle.dev.rastle_backend.domain.*.mapper";
//
//    private final ReplicationDataSourceProperty dataSourceProperties;
//    private final JpaProperties jpaProperties;
//
//
//    /**
//     * 직접 생성한 dataSource 정보를 등록한다.
//     */
//    @Bean
//    public DataSource routingDataSource() {
//        Map<Object, Object> targetDataSources = new LinkedHashMap<>();
//
//        DataSource masterDataSource = createDataSource(
//            dataSourceProperties.getDriverClassName(),
//            dataSourceProperties.getUsername(),
//            dataSourceProperties.getPassword(),
//            dataSourceProperties.getUrl()
//        );
//        targetDataSources.put(DataSourceType.MASTER.getName(), masterDataSource);
//
//        for (Slave slave : dataSourceProperties.getSlaves().values()) {
//            DataSource slaveDataSource = createDataSource(
//                slave.getDriverClassName(),
//                slave.getUsername(),
//                slave.getPassword(),
//                slave.getUrl()
//            );
//            targetDataSources.put(slave.getName(), slaveDataSource);
//        }
//
//        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
//        routingDataSource.setTargetDataSources(targetDataSources);
//        routingDataSource.setDefaultTargetDataSource(masterDataSource);
//
//        return routingDataSource;
//    }
//
//    /**
//     * dataSource 생성
//     * Spring boot 2.0부터 Hikari CP가 default로 적용되므로 type을 HikariDataSource로 설정
//     */
//    private DataSource createDataSource(String driverClassName, String userName, String password, String uri) {
//        return DataSourceBuilder.create()
//            .type(HikariDataSource.class)
//            .driverClassName(driverClassName)
//            .username(userName)
//            .password(password)
//            .url(uri)
//            .build();
//
//    }
//
//    /**
//     * Spring은 transaction 시작 시점에(query를 실행하기 전에) data source connection을 가져온다.
//     * transaction이 시작하면 같은 data source만 사용하게 된다.
//     * TransactionManager 식별 -> DataSource Connection 획득 -> Transaction 동기화(Connection 저장)
//     * 따라서 query를 실행할 시점에 data source connection을 가져올 수 있도록 늦은 연결로 구현해야 한다.
//     */
//    @Bean
//    public DataSource lazyRoutingDataSource(@Qualifier("routingDataSource") DataSource routingDataSource) {
//        return new LazyConnectionDataSourceProxy(routingDataSource);
//    }
//
//    /**
//     * JPA에서 사용하는 EntityManagerFactory 설정
//     * hibernate 설정을 직접 주입
//     */
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("lazyRoutingDataSource") DataSource dataSource) {
//        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
//        return entityManagerFactoryBuilder.dataSource(dataSource)
//            .packages("rastle.dev.rastle_backend.domain")
//            .build();
//    }
//
//    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
//        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), jpaProperties.getProperties(), null);
//    }
//
//    /**
//     * JPA에서 사용할 TransactionManager 설정
//     */
//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory);
//        return transactionManager;
//    }
//}
