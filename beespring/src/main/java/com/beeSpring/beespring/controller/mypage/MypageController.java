package com.beeSpring.beespring.controller.mypage;

import com.beeSpring.beespring.domain.shipping.ShippingAddress;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.PaymentProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.mypage.UserProfileDTO;
import com.beeSpring.beespring.dto.shipping.ShippingAddressDTO;
import com.beeSpring.beespring.dto.user.UserDTO;
import com.beeSpring.beespring.service.mypage.MypageService;
import com.beeSpring.beespring.service.shipping.ShippingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
//@RequestMapping("/mypage")
public class MypageController {
    private final MypageService mypageService;
    private static final Logger log = LoggerFactory.getLogger(MypageController.class);
    private final ShippingService shippingService;


    /**
     * mypage->판매목록
     * serialNumber로 판매 상품을 조회하는 메서드
     *
     * @param serialNumber
     * @return
     */
    @GetMapping(path = "/mypage/salesList/{serialNumber}")
    public ResponseEntity<List<ProductWithSerialNumberDTO>> getSalesList(@PathVariable("serialNumber") String serialNumber) {
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

    /**
     * mypage->구매 목록
     * @param serialNumber
     * @return
     */
    @GetMapping(path = "/mypage/purchaseList/{serialNumber}")
    public ResponseEntity<List<ProductWithSerialNumberDTO>> getPurchaseList(@PathVariable("serialNumber") String serialNumber) {
        // serialNumber를 이용하여 ProductService를 통해 해당 serialNumber에 해당하는 상품 목록을 가져옵니다.
        try {
            List<ProductWithSerialNumberDTO> productList = mypageService.getPurchaseListBySerialNumber(serialNumber);
            if (!productList.isEmpty()) {
                return new ResponseEntity<>(productList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("An error occurred while fetching the purchase list", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 마이페이지->구매목록->결제상태='결제대기'일 경우->결제페이지(serialNumber, productId를 가지고)로 연결
     *
     * @param serialNumber
     * @param productId
     * @return
     */
    @GetMapping("/mypage/productDetails")
    public ResponseEntity<?> getProductDetails(
            @RequestParam("serialNumber") String serialNumber,
            @RequestParam("productId") String productId) {
        try {
            PaymentProductDTO product = mypageService.getProductById(serialNumber, productId);
            if (product != null) {
                return ResponseEntity.ok(product);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("제품 정보 조회 중 오류가 발생했습니다.");
        }
    }

    /**
     * 마이페이지->판매목록 : 검수 완료 된 상품을 판매 시작
     * @param productId
     * @return
     */
    @PutMapping("/mypage/product/{productId}/start-sale")
    public ResponseEntity<String> startSale(@PathVariable String productId) {
        try {
            mypageService.startSale(productId);
            return ResponseEntity.ok("Sale started successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to start sale: " + e.getMessage());
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

            @RequestParam("categoryName") int categoryName,
            @RequestParam("tagName") int tagName,
            @RequestParam("image1") MultipartFile image1,
            @RequestParam("image2") MultipartFile image2,
            @RequestParam(value = "image3", required = false) MultipartFile image3,
            @RequestParam(value = "image4", required = false) MultipartFile image4,
            @RequestParam(value = "image5", required = false) MultipartFile image5) {

        log.info("registerProduct 메서드 시작");

        try {

            String productId = UUID.randomUUID().toString();
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
            productDTO.setPtypeId(categoryName);
            productDTO.setIdolId(tagName);
            productDTO.setTimeLimit(auctionDays * 24 + auctionHours);
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


    //주소 파트
    @GetMapping("/mypage-address/{serialNumber}")
    public ResponseEntity<List<ShippingAddress>> getAddressesBySerialNumber(@PathVariable String serialNumber) {
        List<ShippingAddress> addresses = shippingService.getAddressesBySerialNumber(serialNumber);
        if (addresses.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(addresses);
        }
    }

    @PostMapping("/mypage-address")
    public ResponseEntity<ShippingAddress> createAddress(@RequestBody ShippingAddressDTO addressDTO) {
        log.info("Received addressDTO: {}", addressDTO);
        ShippingAddress createdAddress = shippingService.saveAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAddress);
    }

    @PutMapping("/mypage-address/{id}")
    public ResponseEntity<ShippingAddress> updateAddress(@PathVariable Long id, @RequestBody ShippingAddressDTO addressDTO) {
        Optional<ShippingAddress> existingAddress = shippingService.getAddressById(id);
        if (existingAddress.isPresent()) {
            addressDTO.setAddressId(id);
            ShippingAddress updatedAddress = shippingService.saveAddress(addressDTO);
            return ResponseEntity.ok(updatedAddress);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/mypage-address/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id) {
        shippingService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }

    //프로필 수정 파트
    @GetMapping("/mypage-profile/{serialNumber}")
    public ResponseEntity<UserProfileDTO> getProfile(@PathVariable String serialNumber) {
        UserProfileDTO profileDTO = mypageService.getProfile(serialNumber);
        return ResponseEntity.ok(profileDTO);
    }

    @PutMapping("/mypage-profile/{serialNumber}")
    public ResponseEntity<Void> updateProfile(
            @PathVariable String serialNumber,
            @RequestPart("userDTO") UserProfileDTO userProfileDTO,
            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
        try {
            mypageService.updateProfile(serialNumber, userProfileDTO, profileImageFile);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IOException e) {
            log.error("Error updating profile", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//    @PostMapping(value = "mypage-profile/{serialNumber}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseEntity<String> saveProfile(
//            @PathVariable String serialNumber,
//            @RequestPart("userDTO") UserDTO userDTO,
//            @RequestPart(value = "profileImageFile", required = false) MultipartFile profileImageFile) {
//        try {
//            mypageService.saveProfile(serialNumber, userDTO, profileImageFile);
//            return ResponseEntity.ok("Profile updated successfully");
//        } catch (IOException e) {
//            return ResponseEntity.status(500).body("Failed to update profile: " + e.getMessage());
//        }
//    }

}