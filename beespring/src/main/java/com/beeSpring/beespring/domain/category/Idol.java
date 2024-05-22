package com.beeSpring.beespring.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Idol")
import org.springframework.data.annotation.TypeAlias;

@Entity
@Table(name = "Idol", catalog = "honeypot2")
@Getter
@NoArgsConstructor
public class Idol {
    @Id
    @Column(name = "idol_id")
    private int idolId;

    @Column(name = "idol_name")
    private int idolId;
    private String idolName;
}
