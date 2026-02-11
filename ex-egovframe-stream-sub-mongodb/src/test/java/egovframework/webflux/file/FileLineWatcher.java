package egovframework.webflux.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileLineWatcher implements Runnable {
	private static final int DELAY_MILLIS = 1000;

	private boolean isRun;
	// 대상 파일
	private final File file;

	public FileLineWatcher(File file) {
		this.file = file;
	}

	@Override
	public void run() {
		System.out.println("Start to tail a file - " + file.getName());

		isRun = true;
		if (!file.exists()) {
			System.out.println("Failed to find a file - " + file.getPath());
		}

		// try 문에서 Stream을 열면 블럭이 끝났을 때 close를 해줌
		try (BufferedReader br = new BufferedReader(
			new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

			while (isRun) {
				// readLine(): 파일의 한 line을 읽어오는 메소드
				final String line = br.readLine();
				if (line != null) {
					if (line.length() != 0) {
						log.debug("New line added - " + line);
					} else {
						log.debug("New line NONE");
					}
				} else {
					Thread.sleep(DELAY_MILLIS);
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to tail a file - " + file.getPath());
		}
		System.out.println("Stop to tail a file - " + file.getName());
	}

	private void stringToAscii(String str) {
	String[] strArr = str.split("");
	int[] resultIntArr = new int[strArr.length];

	for (int i = 0; i < strArr.length; i++) {
	    int itemNum = str.charAt(i);
	    resultIntArr[i] = itemNum;
	}
	log.debug("resultIntArr :: " + Arrays.toString(resultIntArr));
	}
	
	/**
	 * 스레드 중지 기능.
	 */
	public void stop() {
		isRun = false;
	}
}