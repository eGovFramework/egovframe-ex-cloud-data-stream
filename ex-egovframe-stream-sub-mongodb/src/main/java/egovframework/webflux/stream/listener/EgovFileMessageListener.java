package egovframework.webflux.stream.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.webflux.stream.dto.EgovFileMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Sinks;

@Slf4j
@RequiredArgsConstructor
//@EnableBinding(EgovFileProcessor.class)
public class EgovFileMessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovFileMessageListener.class);
	
	private final Sinks.Many<String> sink;
	
	//@StreamListener(EgovFileProcessor.INPUT)
	public void handleMessage(String message) {
		log.debug("===> Received File message: " + message);
		
		ObjectMapper mapper = new ObjectMapper();
		EgovFileMessageDTO fileMessageDTO = new EgovFileMessageDTO();
		
		try {
			fileMessageDTO = mapper.readValue(message, EgovFileMessageDTO.class);
		} catch (JsonMappingException e) {
			log.debug(e.getMessage());
		} catch (JsonProcessingException e) {
			log.debug(e.getMessage());
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		
		log.debug("===>>> result = " + fileMessageDTO.toString());

		Map<String, Object> fileStreamInfoMap = new HashMap<>();
		fileStreamInfoMap.put("category", "file");
		fileStreamInfoMap.put("name", fileMessageDTO.getName());
		fileStreamInfoMap.put("uniqueValue", fileMessageDTO.getUniqueValue());
		fileStreamInfoMap.put("byteSize", fileMessageDTO.getByteSize());
		fileStreamInfoMap.put("code", fileMessageDTO.getCode());
		fileStreamInfoMap.put("rowCount", fileMessageDTO.getRowCount());
		
		// Map -> JSON
		String result = "{\"category\":\"\"}";
        try {
			result = mapper.writeValueAsString(fileStreamInfoMap);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        log.debug("websocket send file info : "+result);
        
		sink.emitNext(result, Sinks.EmitFailureHandler.FAIL_FAST);
    	
	}
}