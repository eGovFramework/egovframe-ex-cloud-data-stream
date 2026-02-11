package egovframework.webflux.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="sample")
public class Sample {

    @Id
    private Integer id;
    @Column(value="sample_id")
    private String sampleId;
    @Column(value="name")
    private String name;
    @Column(value="description")
    private String description;
    @Column(value="use_yn")
    private String useYn;
    @Column(value="reg_user")
    private String regUser;

}
