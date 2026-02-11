package egovframework.webflux.service;

import egovframework.webflux.annotation.EgovNotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SampleVO extends SampleDefaultVO {

    private Integer id;
    private String sampleId;
    @EgovNotNull(message="{confirm.required.name}")
    private String name;
    @EgovNotNull(message="{confirm.required.description}")
    private String description;
    private String useYn;
    @EgovNotNull(message="{confirm.required.user}")
    private String regUser;

}
