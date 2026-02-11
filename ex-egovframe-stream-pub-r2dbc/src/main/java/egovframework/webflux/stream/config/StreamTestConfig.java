package egovframework.webflux.stream.config;


import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class StreamTestConfig {
	
    @Bean
    public Supplier<String> basicProducer() {
    	System.out.println("===>>> producer OK");
    	Supplier<String> supplier = () -> {
    		
    		try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		log.debug("===>>> thread = "+Thread.currentThread().getName());
    		
    		return "test message";
    	};
    	
    	return supplier;
    	
    	//return () -> "test message";
    }
    
    @Bean
    public Consumer<String> basicConsumer() {
    	System.out.println("===>>> consumer OK");
    	//return msg -> System.out.println("===>>> message = "+ msg);
    	/*
    	Consumer<String> consumer = (payload) -> {
    		try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		log.debug("===>>> thread = "+Thread.currentThread().getName()+" , message = "+payload);
    	};
    	return consumer;
    	*/
    	return msg -> log.debug("===>>> message = "+ msg);
    }
}