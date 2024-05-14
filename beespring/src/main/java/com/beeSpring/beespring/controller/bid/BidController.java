package com.beeSpring.beespring.controller.bid;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.service.bid.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping(path = "/products")
    public List<ProductWithIdolNameDTO> getAllProductsWithIdolName(){
        return bidService.getAllProductsWithIdolName();
    }

    @GetMapping(path = "/bid-details/{productId}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("productId") String productId) {
        System.out.println(productId);
        // productId를 이용하여 ProductService를 통해 상품 데이터를 가져옵니다.
        ProductDTO productDTO = bidService.getProductById(productId);
        if (productDTO != null) {
            // 상품 데이터가 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            // 상품 데이터가 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
