package com.beeSpring.beespring.service.mypage;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;

import java.util.List;


public interface MypageService {

    List<ProductWithSerialNumberDTO> getProductListBySerialNumber(String serialNumber);


}
