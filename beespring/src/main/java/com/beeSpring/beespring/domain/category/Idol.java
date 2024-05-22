package com.beeSpring.beespring.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Idol")
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Idol {
    @Id
    @Column(name = "idol_id")
    private int idolId;

    @Column(name = "idol_name")
    private String idolName;
}
