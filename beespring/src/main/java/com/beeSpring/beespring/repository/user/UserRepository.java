package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUserId(String userId);
    /**
     * sql injection 을 예방하기 위해 userId를 parameter 로 binding 함
     * */
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findByUserId(@Param("userId") String userId);

    Optional<User> findBySerialNumber(String serialNumber);

}
