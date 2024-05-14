package com.beeSpring.beespring.repository.bid;

import com.beeSpring.beespring.domain.bid.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository  extends JpaRepository<Product, String> {

    @Query("select p, i.idolName from Product p join Idol i on p.idolId = i.idolId")
    List<Object[]> findAllWithIdolName();
}
