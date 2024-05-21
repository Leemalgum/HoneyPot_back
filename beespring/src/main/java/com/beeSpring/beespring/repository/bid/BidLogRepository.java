package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.BidLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidLogRepository extends JpaRepository<BidLog, Integer> {
}
