package com.beeSpring.beespring.dto.category;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class IdolDTO {

    private int idolId;
    private String idolName;

    public IdolDTO() {}
}
