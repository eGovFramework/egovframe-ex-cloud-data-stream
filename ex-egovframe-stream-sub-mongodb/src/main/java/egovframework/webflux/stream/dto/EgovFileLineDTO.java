package egovframework.webflux.stream.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 파일 라인 메시지 페이로드
 */
@Data
@NoArgsConstructor
public class EgovFileLineDTO {

    private int code;
    private String message;
    private String uniqueValue;

}