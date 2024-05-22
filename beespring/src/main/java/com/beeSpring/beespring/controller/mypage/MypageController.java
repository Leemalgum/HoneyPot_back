package com.beeSpring.beespring.controller.mypage;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.service.mypage.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

public class MypageController {
    private final MypageService mypageService;

    @GetMapping(path = "/products/{serialNumber}")
    public ResponseEntity<List<ProductWithSerialNumberDTO>> getProductList(@PathVariable("serialNumber") String serialNumber) {
        // serialNumber를 이용하여 ProductService를 통해 해당 serialNumber에 해당하는 상품 목록을 가져옵니다.
        List<ProductWithSerialNumberDTO> productList = mypageService.getProductListBySerialNumber(serialNumber);
        if (!productList.isEmpty()) {
            // 상품 목록이 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
            return new ResponseEntity<>(productList, HttpStatus.OK);
        } else {
            // 상품 목록이 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
