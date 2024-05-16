package com.beeSpring.beespring.repository.user;

import com.beeSpring.beespring.dto.user.UserTokens;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTokensRepository extends CrudRepository<UserTokens, String> {
}
