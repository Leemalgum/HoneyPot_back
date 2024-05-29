package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.domain.user.OAuthUser;
import com.beeSpring.beespring.domain.user.State;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.user.ManageUserDTO;
import com.beeSpring.beespring.dto.user.ManageUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Boolean existsByUserId(String userId);

    /**
     * 아이디를 직접 입력하는 서비스 자체 로그인
     * sql injection 을 예방하기 위해 userId를 parameter 로 binding 함
     */
    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    User findByUserId(@Param("userId") String userId);

    /**
     * OAuth 서비스에서 provider, user id(key code)가 둘 다 필요할 때 활용
     */
    Optional<User> findByProviderAndUserId(String provider, String UserId);

    /**
     * serialNumber 중복 방지
     */
    boolean existsBySerialNumber(String serialNumber);

    Optional<User> findBySerialNumber(String serialNumber);

    Optional<User> findByFirstNameAndMobileNumber(String firstName, String mobileNumber);

    @Query("SELECT i.idolName FROM UserIdol ui JOIN ui.idol i WHERE ui.user.serialNumber = :serialNumber")
    List<String> findIdolNamesByUserSerialNumber(@Param("serialNumber") String serialNumber);

    @Query("SELECT u FROM User u WHERE u.userId = :userId AND u.mobileNumber = :mobileNumber AND u.email = :email")
    Optional<User> findByUserIdAndMobileNumberAndEmail(@Param("userId") String userId, @Param("mobileNumber") String mobileNumber, @Param("email") String email);

    @Query("SELECT u.serialNumber AS serialNumber, u.userId AS userId, u.gender AS gender, u.birthdate AS birthdate, u.registrationDate AS registrationDate, u.state AS state FROM User u")
    List<ManageUserProjection> findAllUsersAsDTO();

    @Modifying
    @Query("UPDATE User u SET u.state = :state WHERE u.userId = :userId")
    void updateUserState(@Param("userId") String userId, @Param("state") State state);

}
