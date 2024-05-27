package com.beeSpring.beespring.repository.category;

import com.beeSpring.beespring.domain.category.Idol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdolRepository extends JpaRepository<Idol, Integer> {
    Idol findByIdolId(int idolId);
}

