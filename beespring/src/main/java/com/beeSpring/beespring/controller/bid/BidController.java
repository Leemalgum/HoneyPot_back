package com.beeSpring.beespring.controller.bid;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.service.bid.BidService;
import lombok.Getter;
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

    /**
     * '모두보기' 클릭시 보여주는 상품 전체 리스트
     * @return
     */
    @GetMapping(path = "/products")
    public List<ProductWithIdolNameDTO> getAllProductsWithIdolName(){
        return bidService.getAllProductsWithIdolName();
    }

    /**
     * '모두보기'에서 특정 상품 클릭시 보여주는 상품 상세 페이지
     * @param productId
     * @return
     */
    @GetMapping(path = "/bid-details/{productId}")
    public ResponseEntity<MainProductDTO> getProductById(@PathVariable("productId") String productId) {
        // productId를 이용하여 ProductService를 통해 상품 데이터를 가져옵니다.
        MainProductDTO productDTO = bidService.getProductById(productId);
        if (productDTO != null) {
            // 상품 데이터가 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
            return new ResponseEntity<>(productDTO, HttpStatus.OK);
        } else {
            // 상품 데이터가 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}