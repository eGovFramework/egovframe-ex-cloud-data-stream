package egovframework.webflux.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

/**
 * H2 웹 콘솔 기동 (기본 포트 9995)
 */
@Slf4j
@Configuration
public class EgovH2ConsoleConfig {

    private Server webServer;

    @Value("${egov.h2Console.port}")
    private String webPort;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws SQLException {
    	if (webServer == null) {
    		log.debug("===>>> Start H2 Console Server");
    		this.webServer = org.h2.tools.Server.createWebServer("-webPort", webPort).start();
    	}
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        if (this.webServer != null) {
            this.webServer.stop();
        }
    }

}
