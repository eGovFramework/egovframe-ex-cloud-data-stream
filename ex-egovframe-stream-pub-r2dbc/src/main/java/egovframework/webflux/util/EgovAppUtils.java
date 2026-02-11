package egovframework.webflux.util;

import org.springframework.beans.BeanUtils;

import egovframework.webflux.entity.Sample;
import egovframework.webflux.service.SampleVO;

public class EgovAppUtils {

    public static SampleVO entityToVo(Sample sample){
        SampleVO sampleVO = new SampleVO();
        BeanUtils.copyProperties(sample, sampleVO);
        return sampleVO;
    }

    public static Sample voToEntity(SampleVO sampleVO) {
        Sample sample = new Sample();
        BeanUtils.copyProperties(sampleVO, sample);
        return sample;
    }

}
