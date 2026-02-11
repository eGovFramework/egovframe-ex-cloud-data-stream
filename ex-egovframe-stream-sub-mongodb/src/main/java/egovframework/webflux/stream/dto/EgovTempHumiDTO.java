package egovframework.webflux.stream.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EgovTempHumiDTO {

	// DHT-22 
	// 측정 가능 온도 : -40~80C
	// 측정 가능 습도 : 0~100%
	// DHT-11 
	// 측정 가능 온도 : 0~50C
	// 측정 가능 습도 : 20~80%
		
	float temp = -150.0f;
	float humidity = -150.0f; 
}
