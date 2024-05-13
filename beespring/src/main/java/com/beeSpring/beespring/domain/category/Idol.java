package com.beeSpring.beespring.domain.category;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Entity
@Table(name = "Idol", catalog = "honeypot2")
@Getter
@NoArgsConstructor
public class Idol {
    @Id
    private int idolId;
    private String idolName;
}
