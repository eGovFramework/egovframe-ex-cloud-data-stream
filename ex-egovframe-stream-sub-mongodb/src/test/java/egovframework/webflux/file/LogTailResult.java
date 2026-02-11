package egovframework.webflux.file;

/**
 * 로그 전달 객체
 */
public class LogTailResult {

	// 로그를 읽을 시작 위치
	private long startPosition = 0;
	// 읽은 최종 위치
	private long curPosition = 0;
	// 읽은 로그
	private StringBuffer buf = new StringBuffer();

	public long getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(long startPosition) {
		this.startPosition = startPosition;
	}

	public long getCurPosition() {
		return curPosition;
	}

	public void setCurPosition(long curPosition) {
		this.curPosition = curPosition;
	}

	public LogTailResult addResult(String s) {
		buf.append(s);
		return this;
	}

	public String getResults() {
		return buf.toString();
	}
}