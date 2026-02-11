package egovframework.webflux.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 파일의 현재 위치와 현재 위치부터 지정된 라인을 읽을서 전달하도록
 */
public class LogTail {

	// 파일 명
	private String fname;
	// 기본 읽을 라인 수
	private final static int DEF_LINE = 40;

	/**
	 * 파일명으로 객체 생성
	 * 
	 * @param fname
	 */
	public LogTail(String fname) {
		this.fname = fname;
	}

	private void fullLog() {
	}

	/**
	 * 파일 마지막 위치
	 * 
	 * @return 파일의 마지막 위치
	 */
	public long getFileLastPosition() {

		File file = new File(this.fname);
		return file.length();
	}

	/**
	 * 파일의 내용 읽기
	 * 
	 * @param pos 읽기 시작 위치
	 * @return
	 */
	public LogTailResult read(long pos) {
		return read(pos, DEF_LINE);
	}

	/**
	 * 지정된 위치부터 지정된 라인까지 읽어오기
	 * 
	 * @param pos      읽기 시작 위치
	 * @param linesize 읽을 라인 수
	 * @return
	 */
	public LogTailResult read(long pos, int linesize) {

		String curLine;
		int curLineCount = 0;
		long curLastPos = getFileLastPosition();

		// 마지막 위치보다 크게 들어오는 경우 마지막 위치로 설정
		if (pos > 0 && pos > curLastPos) {
			pos = curLastPos;
		}

		LogTailResult logTailResult = new LogTailResult();

		// 파일 읽기 모드
		try (RandomAccessFile raf = new RandomAccessFile(this.fname, "r")) {

			// 읽을 위치로 이동
			if (pos > 0) {
				raf.seek(pos);
			}

			while ((curLine = raf.readLine()) != null) {

				// UTF-8파일의 경우 한글깨짐으로 변환 작업
				String msg = (new String(curLine.getBytes("ISO-8859-1"), "UTF-8"));
				logTailResult.addResult(msg).addResult("\n"); // 개행 문자 추가
				curLineCount++;

				// 읽기 위한 라인 체크
				if (curLineCount >= linesize) {
					break;
				}
			}

			// 현재 위치 함께 전달하여
			// 요청 시 읽기 위한 시점점을 함께 요청
			logTailResult.setCurPosition(raf.getFilePointer());

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return logTailResult;
	}
}