package egovframework.webflux.stream.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by 신용호 2023-06-19
 *
 * Pub/Sub에서 사용할 Data Payload Model
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