package egovframework.webflux.stream.feign;

import lombok.Data;

@Data
public class ErrorMessage {

	int status;
	String code;
	String message;
	String link;
	String developerMessage;
	int total;
	
}
