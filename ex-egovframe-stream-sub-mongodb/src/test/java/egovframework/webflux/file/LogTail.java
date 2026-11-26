package egovframework.webflux.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 지정 위치부터 일정 라인 수만큼 파일을 읽는 tail 유틸
 */
public class LogTail {

	private String fname;
	/** 기본 읽기 라인 수 */
	private final static int DEF_LINE = 40;

	public LogTail(String fname) {
		this.fname = fname;
	}

	/** 파일의 마지막 위치 */
	public long getFileLastPosition() {

		File file = new File(this.fname);
		return file.length();
	}

	/** pos 부터 기본 라인 수만큼 읽기 */
	public LogTailResult read(long pos) {
		return read(pos, DEF_LINE);
	}

	/** pos 부터 linesize 라인만큼 읽고 최종 위치를 함께 반환 */
	public LogTailResult read(long pos, int linesize) {

		String curLine;
		int curLineCount = 0;
		long curLastPos = getFileLastPosition();

		// 파일 끝을 넘는 위치는 끝으로 보정
		if (pos > 0 && pos > curLastPos) {
			pos = curLastPos;
		}

		LogTailResult logTailResult = new LogTailResult();

		try (RandomAccessFile raf = new RandomAccessFile(this.fname, "r")) {

			if (pos > 0) {
				raf.seek(pos);
			}

			while ((curLine = raf.readLine()) != null) {

				// readLine() 은 ISO-8859-1 로 읽으므로 UTF-8 로 재변환
				String msg = (new String(curLine.getBytes("ISO-8859-1"), "UTF-8"));
				logTailResult.addResult(msg).addResult("\n");
				curLineCount++;

				if (curLineCount >= linesize) {
					break;
				}
			}

			// 다음 읽기 시작 위치
			logTailResult.setCurPosition(raf.getFilePointer());

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		return logTailResult;
	}
}