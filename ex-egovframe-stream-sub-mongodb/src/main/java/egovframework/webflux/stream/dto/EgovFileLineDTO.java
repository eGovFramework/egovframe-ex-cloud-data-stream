package egovframework.webflux.stream.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 신용호 2023-09-12
 *
 * Pub/Sub에서 사용할 Data Payload Model
 */
@Data
@NoArgsConstructor
public class EgovFileLineDTO {

    private int code;
    private String message;
    private String uniqueValue;

}