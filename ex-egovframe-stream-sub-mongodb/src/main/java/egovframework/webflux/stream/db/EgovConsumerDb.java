package egovframework.webflux.stream.db;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

/**
 * db-topic(DB 처리 이력) 수신 → WebSocket(/ws/db) 전달
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerDb {

    private final Sinks.Many<String> sinkDb;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public Consumer<String> historyDb() {
        return payload -> {
            log.debug("===> Received DB message: {}", payload);

            String category = "";
            try {
                JsonNode jsonNode = objectMapper.readTree(payload);
                category = jsonNode.path("category").asText("");
            } catch (Exception e) {
                log.error("JSON 파싱 오류 : {}", e.getMessage());
            }
            log.debug("===>>> category = {}", category);

            sinkDb.emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
        };
    }

}
