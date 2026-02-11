package egovframework.webflux.stream.processor;

// Spring Cloud Stream 4.x에서는 @Input, @Output 어노테이션이 제거되었습니다.
// 함수형 프로그래밍 모델(Consumer, Function, Supplier)을 사용하세요.
@Deprecated
public interface EgovSensorProcessor {
	String INPUT = "sensor-in";
}