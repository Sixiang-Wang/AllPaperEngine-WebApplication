package com.example.scholar.domain.openalex;

import com.example.scholar.dto.InstitutionsResultDto;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
@Data
public class Institutions {
    private String id;
    private String ror;//学术组织标识符
    private String displayName;
    private String countryCode;
    private String type;
    private String homapageUrl;
    private String imageUrl;
    private String imageThumbnailUrl;
    private String displayNameAcronyms;
    private String displayNameAlternatives;
    private int worksCouont;
    private int citedByCount;
    private String worksApiUrl;
    private String updatedDate;

    public InstitutionsResultDto toDto() {
        InstitutionsResultDto res = new InstitutionsResultDto();

        res.setType(this.type);
        res.setRor(this.ror);
//        res.setLineage(this.);貌似没有这个属性了
        res.setDisplay_name(this.displayName);
        res.setCountry_code(this.countryCode);
        return res;
    }
}
