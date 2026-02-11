package egovframework.webflux.stream.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultVO {

	// DHT-22 
	// 측정 가능 온도 : -40~80C
	// 측정 가능 습도 : 0~100%
	// DHT-11 
	// 측정 가능 온도 : 0~50C
	// 측정 가능 습도 : 20~80%
		
	int resultCode = 200;
	String resultMessage = "OK"; 
}
