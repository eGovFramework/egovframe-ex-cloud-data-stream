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

/**
 * 파일을 라인 단위로 읽어 line-topic 으로 발행
 */
@Slf4j
@Configuration
public class EgovStreamLineFile {

    /** 1회 발행 시 읽을 최대 라인 수 */
    private static final int PAGE_COUNT = 10;

    @Value("${egov.lineFilePath}")
    private String filePath;

    private BufferedReader br;

    /** poller 주기마다 추가된 라인을 묶어 발행 (추가분이 없으면 null 반환 → 미발행) */
    @Bean
    public Supplier<String> lineFileProducer() {
        File file = new File(filePath);
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
            log.debug("===>>> lineFile producer OK : {}", file.getAbsolutePath());
        } catch (FileNotFoundException e) {
            log.error("===>>> lineFile not found : {}", file.getAbsolutePath());
        }

        return () -> {
            if (br == null) {
                return null;
            }

            StringBuilder resultLine = new StringBuilder();
            for (int i = 0; i < PAGE_COUNT; i++) {
                try {
                    String line = br.readLine();
                    if (line == null) {
                        log.debug("line EOF");
                        break;
                    }
                    if (line.isEmpty()) {
                        log.debug("New line NONE");
                        continue;
                    }
                    log.debug("New line added - {}", line);
                    if (resultLine.length() > 0) {
                        resultLine.append("\n");
                    }
                    resultLine.append(line);
                } catch (IOException e) {
                    log.error("===>>> lineFile read error : {}", e.getMessage());
                    break;
                }
            }

            if (resultLine.length() == 0) {
                log.debug("===>>> No data has been added.");
                return null;
            }
            return resultLine.toString();
        };
    }

}
