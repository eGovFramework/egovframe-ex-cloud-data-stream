package egovframework.webflux.file;

/**
 * tail 읽기 결과 (내용, 위치)
 */
public class LogTailResult {

	/** 읽기 시작 위치 */
	private long startPosition = 0;
	/** 읽기 종료 위치 */
	private long curPosition = 0;
	/** 읽은 내용 */
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