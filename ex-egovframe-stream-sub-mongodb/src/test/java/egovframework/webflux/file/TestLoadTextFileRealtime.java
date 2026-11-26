package egovframework.webflux.file;

import java.io.File;

/**
 * FileLineWatcher 수동 실행 예제 (30초간 감시)
 */
public class TestLoadTextFileRealtime {

	public static void main(String[] args) {

		loadFileRealtime1();
	}
	
	public static void loadFileRealtime1() {

		File file = new File("../ex-egovframe-stream-pub-r2dbc/src/main/resources/file/txtData2.csv");
		FileLineWatcher watcher = new FileLineWatcher(file);
		Thread thread = new Thread(watcher);
		thread.setDaemon(true);
		thread.start();

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}

		watcher.stop();
	}

}
