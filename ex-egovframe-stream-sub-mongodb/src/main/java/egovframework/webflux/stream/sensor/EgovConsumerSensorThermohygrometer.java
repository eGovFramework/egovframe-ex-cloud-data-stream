package egovframework.webflux.stream.sensor;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.webflux.stream.dto.EgovTempHumiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

/**
 * RabbitMQ 온습도 센서 메시지 수신 → WebSocket(/ws/sensor) 전달
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerSensorThermohygrometer {

    private final Sinks.Many<String> sink;

    @Bean
    public Consumer<String> sensorThermo() {
        return payload -> {
            log.debug("===> Received Sensor message: {}", payload);

            EgovTempHumiDTO tempHumiVO = new EgovTempHumiDTO();
            try {
                tempHumiVO = new ObjectMapper().readValue(payload, EgovTempHumiDTO.class);
            } catch (Exception e) {
                log.debug(e.getMessage());
            }

            log.debug("===>>> result = {}", tempHumiVO);
            sink.emitNext(payload, Sinks.EmitFailureHandler.FAIL_FAST);
        };
    }

}
