package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.OAuthUser;
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
     * 아이디를 직접 입력하는 서비스 자체 로그인
     * sql injection 을 예방하기 위해 userId를 parameter 로 binding 함
     * */
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findByUserId(@Param("userId") String userId);

    /**
     * OAuth 서비스에서 provider, user id(key code)가 둘 다 필요할 때 활용
     * */
    Optional<User> findByProviderAndUserId(String provider, String UserId);

    /**
     * serialNumber 중복 방지
     * */
    boolean existsBySerialNumber(String serialNumber);

    Optional<User> findBySerialNumber(String serialNumber);

    Optional<User> findByFirstNameAndMobileNumber(String firstName, String mobileNumber);
}
