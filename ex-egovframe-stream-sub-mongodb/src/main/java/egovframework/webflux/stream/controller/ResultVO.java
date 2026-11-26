package egovframework.webflux.stream.controller;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 처리 결과 응답
 */
@Getter
@Setter
@ToString
public class ResultVO {

    int resultCode = 200;
    String resultMessage = "OK";

}
