package egovframework.webflux.stream.aspect.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DefaultDTO {

	private String category;
	private String className;
	private String methodName;
    private Date currentDateTime = new Date();
    private long elapsedMills = 0;

}
