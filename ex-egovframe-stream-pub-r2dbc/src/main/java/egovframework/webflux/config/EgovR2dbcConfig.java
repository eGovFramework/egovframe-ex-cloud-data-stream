package egovframework.webflux.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.h2.H2ConnectionOption;
import io.r2dbc.spi.ConnectionFactory;

/**
 * R2DBC(H2 인메모리) 설정 및 스키마 초기화
 */
@Configuration
@EnableR2dbcRepositories(basePackages={"egovframework.webflux.repository"})
public class EgovR2dbcConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(H2ConnectionConfiguration.builder()
                .inMemory("sampledb")
                .property(H2ConnectionOption.DB_CLOSE_DELAY,"-1") // 연결 종료 후에도 데이터 유지
                .username("sa")
                .build());
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);
        initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("db/sampledb.sql")));
        return initializer;
    }

}
