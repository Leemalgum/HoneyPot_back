package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.UserIdol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserIdolRepository extends JpaRepository<UserIdol, Integer> {
//    List<UserIdol> findBySerialNumber(String serialNumber);
}
