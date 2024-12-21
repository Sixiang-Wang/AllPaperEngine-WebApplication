package com.example.scholar.domain;

import com.example.scholar.dto.net.NetDataType;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.List;

@Component
@Data
public class AuthorForNet {
    private String id;
    @Column(columnDefinition = "json")
    private String displayNameAlterNatives;
    private String name;
    @Transient
    private NetDataType type;
    List<AuthorForNet> relatedAuthors;
}
