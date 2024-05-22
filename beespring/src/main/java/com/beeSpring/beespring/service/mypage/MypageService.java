package com.beeSpring.beespring.service.mypage;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


public interface MypageService {

    List<ProductWithSerialNumberDTO> getProductListBySerialNumber(String serialNumber);



}
