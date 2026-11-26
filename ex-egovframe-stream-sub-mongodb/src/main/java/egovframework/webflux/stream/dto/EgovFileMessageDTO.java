package egovframework.webflux.stream.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 파일 정보 메시지 페이로드
 */
@Data
@NoArgsConstructor
public class EgovFileMessageDTO {

    private int code;
    private String message;
    private String name;
    private int rowCount;
    private int byteSize;
    private String uniqueValue;

}