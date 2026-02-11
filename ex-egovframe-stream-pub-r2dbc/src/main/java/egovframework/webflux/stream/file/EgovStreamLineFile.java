package egovframework.webflux.stream.file;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EgovStreamLineFile {
	
	private static int PAGE_COUNT = 10;
	private static final int DELAY_MILLIS = 50;
	
	// path = /Volumes/EXSSD/EGOV/temp/txtData2.csv
    @Value("${egov.lineFilePath}")
    private String filePath;
	
	// 대상 파일
	private File file;
	private BufferedReader br;
	
    @Bean
    public Supplier<String> lineFileProducer() {

    	file = new File(filePath);
    	
    	log.debug("===>>> lineFile producer OK");
		
		try {
			br = new BufferedReader(
					new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
    	Supplier<String> lineFileSupplier = () -> {
    		
    		String resultLine = "";
    		
    		// try 문에서 Stream을 열면 블럭이 끝났을 때 close를 해줌
    			for(int i=0 ; i<PAGE_COUNT ; i++) {
    				// readLine(): 파일의 한 line을 읽어오는 메소드
    				String line;
					try {
						line = br.readLine();
	    				if (line != null) {
	    					if (line.length() != 0) {
	    						log.debug("New line added - " + line);
	    						if ("".equals(resultLine)) {
	    							resultLine = line;
	    						} else {
	    							resultLine = resultLine + "\n" + line;
	    						}
	    						/*
	        					try {
	    							Thread.sleep(DELAY_MILLIS);
	    						} catch (InterruptedException e) {
	    							e.printStackTrace();
	    						}
	    						*/
	    					} else {
	    						log.debug("New line NONE");
	    					}
	    				} else {
	    					log.debug("line EOF");
	    					break;
	    				}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
    				}
    			}
    		if ( "".equals(resultLine)) {
    			resultLine = null;
        		log.debug("===>>> No data has been added.");
    		}
    		
    		return resultLine;
    	};
    	return lineFileSupplier;
    }
    
	/*
    @Bean
    public Consumer<String> basicConsumer() {
    	System.out.println("===>>> consumer OK");
    	return msg -> System.out.print("===>>> message = "+ msg);
    }
    */
}