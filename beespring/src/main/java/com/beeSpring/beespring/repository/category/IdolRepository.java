package com.beeSpring.beespring.repository.category;

import com.beeSpring.beespring.domain.category.Idol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IdolRepository extends JpaRepository<Idol, Integer> {
    Idol findByIdolId(int idolId);

    @Query("SELECT i.idolId FROM Idol i WHERE i.idolName = :name")
    Integer findIdByName(String name);
}

