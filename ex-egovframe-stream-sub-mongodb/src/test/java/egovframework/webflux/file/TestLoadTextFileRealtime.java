package egovframework.webflux.file;

import java.io.File;

public class TestLoadTextFileRealtime {

	public static void main(String[] args) {

		loadFileRealtime1();
	}
	
	public static void loadFileRealtime1() {

		File file = new File("/Volumes/EXSSD/EGOV/temp/txtData2.csv");
		FileLineWatcher watcher = new FileLineWatcher(file);
		Thread thread = new Thread(watcher);
		thread.setDaemon(true);
		thread.start();

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 만약 종료하고 싶다면
		watcher.stop();
	}

}
