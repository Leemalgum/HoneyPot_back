package com.beeSpring.beespring.controller.mypage;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.service.mypage.MypageService;
import com.beeSpring.beespring.service.shipping.ShippingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor

public class MypageController {
    private final MypageService mypageService;
    private static final Logger log = LoggerFactory.getLogger(MypageController.class);
    private final ShippingService shippingService;


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

    @GetMapping(path = "/purchaseList/{serialNumber}")
    public ResponseEntity<List<ProductWithSerialNumberDTO>> getPurchaseList(@PathVariable("serialNumber") String serialNumber) {
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


    //입고요청
    @PostMapping(value = "/mypage-stock", consumes = "multipart/form-data")
    public ResponseEntity<String> registerProduct(
            @RequestParam("productName") String productName,
            @RequestParam("priceUnit") Integer priceUnit,
            @RequestParam("buyNow") Integer buyNow,
            @RequestParam("startPrice") Integer startPrice,
            @RequestParam("productInfo") String productInfo,
            @RequestParam("auctionDays") int auctionDays,
            @RequestParam("auctionHours") int auctionHours,
            @RequestParam("categoryName") String categoryName,
            @RequestParam("tagName") String tagName,
            @RequestParam("image1") MultipartFile image1,
            @RequestParam("image2") MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4,
            @RequestParam(value = "image5", required = false) MultipartFile image5) {

        log.info("registerProduct 메서드 시작");

        try {
            // 아이돌 이름의 첫 두 글자와 UUID를 조합하여 productId 생성
            String idolPrefix = tagName.substring(0, Math.min(tagName.length(), 2)).toUpperCase();
            String productId = idolPrefix + "-" + UUID.randomUUID().toString();
            ProductDTO productDTO = new ProductDTO();
            productDTO.setProductId(productId);
            productDTO.setProductName(productName);
            productDTO.setPriceUnit(priceUnit);
            productDTO.setBuyNow(buyNow);
            productDTO.setStartPrice(startPrice);
            productDTO.setProductInfo(productInfo);

            log.info("Received request to register product: {}", productDTO);
            log.info("Received images: image1={}, image2={}, image3={}, image4={}, image5={}",
                    image1.getOriginalFilename(), image2.getOriginalFilename(),
                    image3 != null ? image3.getOriginalFilename() : "null",
                    image4 != null ? image4.getOriginalFilename() : "null",
                    image5 != null ? image5.getOriginalFilename() : "null");
            log.info("auctionDays={}, auctionHours={}, categoryName={}, tagName={}", auctionDays, auctionHours, categoryName, tagName);

            if (image1 != null && !image1.isEmpty()) {
                log.info("image1 업로드 시작");
                productDTO.setImage1(mypageService.storeImage(image1));
                log.info("image1 업로드 완료");
            }
            if (image2 != null && !image2.isEmpty()) {
                log.info("image2 업로드 시작");
                productDTO.setImage2(mypageService.storeImage(image2));
                log.info("image2 업로드 완료");
            }
            if (image3 != null && !image3.isEmpty()) {
                log.info("image3 업로드 시작");
                productDTO.setImage3(mypageService.storeImage(image3));
                log.info("image3 업로드 완료");
            }
            if (image4 != null && !image4.isEmpty()) {
                log.info("image4 업로드 시작");
                productDTO.setImage4(mypageService.storeImage(image4));
                log.info("image4 업로드 완료");
            }
            if (image5 != null && !image5.isEmpty()) {
                log.info("image5 업로드 시작");
                productDTO.setImage5(mypageService.storeImage(image5));
                log.info("image5 업로드 완료");
            }

            productDTO.setSerialNumber("123456789");
            productDTO.setPtypeId(mapCategoryToId(categoryName));
            productDTO.setIdolId(mapTagToId(tagName));
            productDTO.setTimeLimit(auctionDays*24+auctionHours);
            productDTO.setStorageStatus("PENDING");
            productDTO.setView(0);
            productDTO.setRequestTime(LocalDateTime.now());
            productDTO.setRegistrationDate(LocalDateTime.now());

            log.info("registerProduct 메서드 끝 - ProductDTO: {}", productDTO);

            mypageService.registerProduct(productDTO);
            return ResponseEntity.ok("Product registered successfully.");

        } catch (Exception e) {
            log.error("Error occurred while registering product", e);
            return ResponseEntity.internalServerError().body("Failed to register product: " + e.getMessage());
        }
    }

    private int mapCategoryToId(String categoryName) {
        Map<String, Integer> categoryMap = Map.of(
                "Photo", 1,
                "Official Fanlight", 2,
                "Fashion", 3,
                "Acc", 4,
                "Stationery", 5,
                "DVD", 6,
                "Music", 7,
                "Living", 8
        );
        return categoryMap.getOrDefault(categoryName, 0);
    }

    private int mapTagToId(String tagName) {
        Map<String, Integer> tagMap = Map.ofEntries(
                Map.entry("AESPA", 1),
                Map.entry("BLACKPINK", 2),
                Map.entry("BOYNEXTDOOR", 3),
                Map.entry("BTS", 4),
                Map.entry("ENHYPEN", 5),
                Map.entry("EXO", 6),
                Map.entry("GIRLSRENERATION", 7),
                Map.entry("ITZY", 8),
                Map.entry("LESSERAFIM", 9),
                Map.entry("NCT", 10),
                Map.entry("NEWJEANS", 11),
                Map.entry("NMIXX", 12),
                Map.entry("FROMIS_9", 13),
                Map.entry("RIIZE", 14),
                Map.entry("STRAYKIDS", 15),
                Map.entry("SEVENTEEN", 16),
                Map.entry("SHINEE", 17),
                Map.entry("SUPERJUNIOR", 18),
                Map.entry("TXT", 19),
                Map.entry("TWICE", 20),
                Map.entry("WINNER", 21)
        );
        return tagMap.getOrDefault(tagName, 0);
    }

    //주소 파트
    @GetMapping("/{id}")
    public ResponseEntity<ShippingAddress> getAddressById(@PathVariable Long id) {
        Optional<ShippingAddress> address = shippingService.getAddressById(id);
        return address.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ShippingAddress createAddress(@RequestBody ShippingAddressDTO addressDTO) {
        return shippingService.saveAddress(addressDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddress> updateAddress(@PathVariable Long id, @RequestBody ShippingAddressDTO addressDTO) {
        Optional<ShippingAddress> existingAddress = shippingService.getAddressById(id);
        if (existingAddress.isPresent()) {
            addressDTO.setAddressId(id);
            return ResponseEntity.ok(shippingService.saveAddress(addressDTO));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        shippingService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}