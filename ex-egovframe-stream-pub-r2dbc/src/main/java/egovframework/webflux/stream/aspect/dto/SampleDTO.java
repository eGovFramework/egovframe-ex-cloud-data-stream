package egovframework.webflux.stream.aspect.dto;

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
public class SampleDTO extends DefaultDTO {

    private Sample content;
    private Sort sort;
    
}
