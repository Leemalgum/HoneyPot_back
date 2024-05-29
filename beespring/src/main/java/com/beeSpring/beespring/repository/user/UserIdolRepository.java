package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.domain.user.UserIdol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserIdolRepository extends JpaRepository<UserIdol, Integer> {
//    List<UserIdol> findBySerialNumber(String serialNumber);

    @Transactional
    @Modifying
    @Query("DELETE FROM UserIdol ui WHERE ui.user = :user")
    void deleteByUser(User user);

}
