package com.beeSpring.beespring.domain.category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;

@Entity
@Table(name = "Product_type")
@Getter
@NoArgsConstructor
public class ProductType {
    @Id
    @Column(name = "ptype_id")
    private int ptypeId;

    @Column(name = "ptype_name")
    private String ptypeName;
}
