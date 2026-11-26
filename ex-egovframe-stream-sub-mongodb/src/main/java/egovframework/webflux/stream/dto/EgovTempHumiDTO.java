package egovframework.webflux.stream.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 온습도 센서 측정값.
 * 측정 범위 - DHT-22 : -40~80℃, 0~100% / DHT-11 : 0~50℃, 20~80%
 */
@Getter
@Setter
@ToString
public class EgovTempHumiDTO {

    /** 초기값 -150 : 미수신 */
    float temp = -150.0f;
    float humidity = -150.0f;

}
