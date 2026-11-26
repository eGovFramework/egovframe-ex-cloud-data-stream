package egovframework.webflux.stream.api;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import egovframework.webflux.repository.RealtimePositionRepository;
import egovframework.webflux.stream.api.dto.RealtimePositionDTO;
import egovframework.webflux.stream.api.entity.RealtimePosition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * api-topic(지하철 실시간 위치) 수신 → MongoDB 저장
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class EgovConsumerTrainApi {

    @Bean
    public Consumer<List<RealtimePositionDTO>> trainApi(RealtimePositionRepository realtimePositionRepository) {
        return payload -> {
            log.debug("===> Received Train API message: {}", payload);

            long currentTimeStamp = System.currentTimeMillis();
            for (RealtimePositionDTO realtimePositionDTO : payload) {
                RealtimePosition realtimePosition = new RealtimePosition();
                BeanUtils.copyProperties(realtimePositionDTO, realtimePosition);
                realtimePosition.setReceivedTimeStamp(currentTimeStamp);
                log.debug("===> realtimePosition : {}", realtimePosition);

                realtimePositionRepository.save(realtimePosition).subscribe();
            }
        };
    }

}
