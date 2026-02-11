package egovframework.webflux.stream.feign;

import java.util.List;

import lombok.Data;

@Data
public class ResultTrainPosition {
	
	ErrorMessage errorMessage;
	List<RealtimePosition> realtimePositionList;

}
