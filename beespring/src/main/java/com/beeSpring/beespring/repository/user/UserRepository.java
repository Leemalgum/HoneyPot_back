package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(String userId);

}
