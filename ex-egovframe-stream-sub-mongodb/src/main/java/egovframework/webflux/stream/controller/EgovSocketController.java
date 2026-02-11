package egovframework.webflux.stream.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.webflux.annotation.EgovController;
import egovframework.webflux.stream.dto.EgovTempHumiDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@EgovController
@Slf4j
@RequiredArgsConstructor
public class EgovSocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSocketController.class);
    private final Sinks.Many<String> sink;

    @Value("${egov.websocket.host}")
    private String websocketHost;
    
    @Value("${server.port}")
    private String serverPort;

    @GetMapping("/sensor")
    public String sensor(Model model) {
        String websocketUrl = String.format("ws://%s:%s/ws/sensor", websocketHost, serverPort);
        model.addAttribute("websocketUrl", websocketUrl);
        return "egovSensor";
    }

    @GetMapping("/file_line")
    public String fileLine(Model model) {
        String websocketUrl = String.format("ws://%s:%s/ws/file", websocketHost, serverPort);
        model.addAttribute("websocketUrl", websocketUrl);
        return "egovConsumerFileLine";
    }

    @GetMapping("/db")
    public String db(Model model) {
        String websocketUrl = String.format("ws://%s:%s/ws/db", websocketHost, serverPort);
        model.addAttribute("websocketUrl", websocketUrl);
        return "egovConsumerDb";
    }

    @GetMapping("/echo")
    public String echo() {
        return "egovEcho";
    }

    @ResponseBody
    @PostMapping(value="/pingSensor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultVO pingSensor(EgovTempHumiDTO tempHumiVO) {
    	
    	ResultVO result = new ResultVO();
    	ObjectMapper mapper = new ObjectMapper();
    	String resultJson = "{}";
    	try {
			resultJson = mapper.writeValueAsString(tempHumiVO);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
		}
    	LOGGER.info("===>>> "+resultJson);
    	//String message = "hello";
    	sink.emitNext(resultJson, Sinks.EmitFailureHandler.FAIL_FAST);
    	
    	return result;
    }

    
}
