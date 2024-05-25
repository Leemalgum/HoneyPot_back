package com.beeSpring.beespring.controller.bid;
import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.service.bid.BidLogService;
import com.beeSpring.beespring.service.bid.BidService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequiredArgsConstructor

public class BidController {
    private final BidService bidService;
    private final BidLogService bidLogService;
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
        bidService.increaseViewCount(productId);
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
    @PostMapping(path = "/bids/place")
    public ResponseEntity<Bid> placeBid(@RequestParam String productId, @RequestParam String serialNumber, @RequestParam int bidAmount) {
        try {
            System.out.println("Received bid data - productId: " + productId + ", serialNumber: " + serialNumber + ", bidAmount: " + bidAmount);
            // Product 객체를 가져옴
            Product product = bidService.getProductEntityById(productId);
            if (product == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            // User 객체를 시리얼 넘버로 생성
            User user = new User();
            user.setSerialNumber(serialNumber);
            // 입찰 수행
            Bid bid = bidLogService.placeBid(product, user, bidAmount);
            System.out.println("Bid placed successfully for product: " + productId + ", by user: " + serialNumber);
            return new ResponseEntity<>(bid, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            System.out.println("Bid placement failed due to illegal argument: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            System.out.println("Bid placement failed due to unexpected error: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}