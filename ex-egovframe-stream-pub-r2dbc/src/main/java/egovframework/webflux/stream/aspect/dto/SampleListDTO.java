package egovframework.webflux.stream.aspect.dto;

import java.util.List;

import org.springframework.data.domain.Sort;

import egovframework.webflux.entity.Sample;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SampleListDTO extends DefaultDTO {

    private Sample search;
    private List<Sample> contentList;
    private Sort sort;
    
}
