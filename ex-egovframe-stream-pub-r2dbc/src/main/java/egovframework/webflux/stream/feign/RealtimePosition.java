package egovframework.webflux.stream.feign;

import lombok.Data;

@Data
public class RealtimePosition {

	int beginRow = 0;
	int endRow = 0;
	int curPage = 0;
	int pageRow = 0;
	int totalCount; // 74
	int rowNum; // 1
	int selectedCount; // 5
	String subwayId; // 1001
	String subwayNm; // 1호선
	String statnId; // 1001000133
	String statnNm; // 서울
	String trainNo; // 0060
	String lastRecptnDt; // 20231101
	String recptnDt; // 2023-11-01 10:52:22
	int updnLine; // 0
	String statnTid; // 1001000110
	String statnTnm; // 의정부
	int trainSttus; // 0
	int directAt; // 0
	int lstcarAt; // 0

}
