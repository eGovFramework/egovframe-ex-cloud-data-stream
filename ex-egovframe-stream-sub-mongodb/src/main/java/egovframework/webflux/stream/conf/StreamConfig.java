package egovframework.webflux.stream.conf;


import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StreamConfig {
	
    //@Bean
    public Supplier<String> basicProducer() {
    	log.debug("===>>> Producer BEAN");
    	return () -> "test message";
    }
    
    //@Bean
    public Consumer<String> basicConsumer() {
    	System.out.println("===>>> Consumer BEAN");
    	return msg -> log.debug("===>>> message = {}", msg);
    }
}