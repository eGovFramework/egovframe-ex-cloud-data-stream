package egovframework.webflux.stream.feign;

import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

@Getter
@XmlRootElement(name = "realtimePosition")
public class XmlResultTrainPosition {
	
	@XmlElement(name = "RESULT")
	ErrorMessage errorMessage;
	
	@XmlElement(name = "row")
	List<RealtimePosition> realtimePositionList;

}
