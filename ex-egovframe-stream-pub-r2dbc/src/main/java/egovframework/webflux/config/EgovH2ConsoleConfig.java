package egovframework.webflux.config;

import java.sql.SQLException;

import org.h2.tools.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EgovH2ConsoleConfig {

    private Server webServer;

    @EventListener(ContextRefreshedEvent.class)
    public void start() throws SQLException {
    	if (webServer == null) {
    		log.debug("===>>> Start H2 Console Server");
    		this.webServer = org.h2.tools.Server.createWebServer("-webPort", "9995", "-tcpAllowOthers").start();
    	}
    }

    @EventListener(ContextClosedEvent.class)
    public void stop() {
        this.webServer.stop();
    }

}
