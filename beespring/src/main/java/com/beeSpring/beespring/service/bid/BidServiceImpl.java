package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.domain.bid.*;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.bid.BidLogDTO;
import com.beeSpring.beespring.dto.bid.BidResultDTO;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.repository.bid.BidLogRepository;
import com.beeSpring.beespring.repository.bid.BidResultRepository;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import com.beeSpring.beespring.repository.main.MainProductRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDate;
import java.time.LocalTime;
import com.beeSpring.beespring.dto.bid.PendingProductsDTO;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService{

    private final ProductRepository productRepository;
    private final MainProductRepository mainProductRepository;
    private final BidResultRepository bidResultRepository;
    private final UserRepository userRepository;
    private final BidLogRepository bidLogRepository;

    public List<ProductWithIdolNameDTO> getAllProductsWithIdolName(){
        List<Object[]> productList = productRepository.findAllWithIdolName();
        List<ProductWithIdolNameDTO> products = new ArrayList<>();

        for (Object[] objArray : productList) {
            Product product = (Product) objArray[0];
            String idolName = (String) objArray[1];
            ProductWithIdolNameDTO productDTO = new ProductWithIdolNameDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setProductName(product.getProductName());
            productDTO.setIdolName(idolName);
            productDTO.setPtypeId(product.getProductType().getPtypeId());
            productDTO.setSerialNumber(product.getUser().getSerialNumber());
            productDTO.setImage1(product.getImage1());
            productDTO.setImage2(product.getImage2());
            productDTO.setImage3(product.getImage3());
            productDTO.setImage4(product.getImage4());
            productDTO.setImage5(product.getImage5());
            productDTO.setProductInfo(product.getProductInfo());
            productDTO.setPrice(product.getPrice());
            productDTO.setPriceUnit(product.getPriceUnit());
            productDTO.setBuyNow(product.getBuyNow());
            productDTO.setTimeLimit(product.getTimeLimit());
            productDTO.setView(product.getView());
            productDTO.setStartPrice(product.getStartPrice());
            productDTO.setRegistrationDate(product.getRegistrationDate());
            productDTO.setBidCnt(product.getBidCnt());
            productDTO.setRequestTime(product.getRequestTime());
            productDTO.setStorageStatus(String.valueOf(product.getStorageStatus()));
            productDTO.setPtypeName(product.getProductType().getPtypeName());

            products.add(productDTO);
        }

        return products;
    }

    @Override
    public MainProductDTO getProductById(String productId) {
        Product product = mainProductRepository.findById(productId).stream().findFirst().get();
        String userId = getUserIdByProductId(productId);

        MainProductDTO productDTO = MainProductDTO.builder()
                .productId(product.getProductId())
                .idolId(product.getIdol().getIdolId())
                .ptypeId(product.getProductType().getPtypeId())
                .userId(userId)
                .serialNumber(product.getProductId())
                .productName(product.getProductName())
                .image1(product.getImage1())
                .image2(product.getImage2())
                .image3(product.getImage3())
                .image4(product.getImage4())
                .image5(product.getImage5())
                .productInfo(product.getProductInfo())
                .price(product.getPrice())
                .priceUnit(product.getPriceUnit())
                .buyNow(product.getBuyNow())
                .timeLimit(product.getTimeLimit())
                .view(product.getView())
                .startPrice(product.getStartPrice())
                .registrationDate(product.getRegistrationDate())
                .bidCnt(product.getBidCnt())
                .requestTime(product.getRequestTime())
                .storageStatus(String.valueOf(product.getStorageStatus()))
                .build();

        return productDTO;
    }
    @Override
    public Product getProductEntityById(String productId) {
        Product product = productRepository.findById(productId).stream().findFirst().get();

        ProductDTO productDTO = ProductDTO.builder()
                .productId(product.getProductId())
                .idolId(product.getIdol().getIdolId())
                .ptypeId(product.getProductType().getPtypeId())
                .serialNumber(product.getProductId())
                .productName(product.getProductName())
                .image1(product.getImage1())
                .image2(product.getImage2())
                .image3(product.getImage3())
                .image4(product.getImage4())
                .image5(product.getImage5())
                .productInfo(product.getProductInfo())
                .price(product.getPrice())
                .priceUnit(product.getPriceUnit())
                .buyNow(product.getBuyNow())
                .timeLimit(product.getTimeLimit())
                .view(product.getView())
                .startPrice(product.getStartPrice())
                .registrationDate(product.getRegistrationDate())
                .bidCnt(product.getBidCnt())
                .requestTime(product.getRequestTime())
                .storageStatus(String.valueOf(product.getStorageStatus()))
                .build();

        return productDTO.toEntity();
    }

    @Override
    public String getUserIdByProductId(String productId) {
        String userId = mainProductRepository.findUserIdByProductId(productId);
        return userId;
    }



    @Transactional
    @Override
    public void increaseViewCount(String productId) {
        productRepository.incrementViewCount(productId);
    }

    @Override
    public void updateProductStatusAfterConfirm(String productId, String storageStatus) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
//        product.setStatus(storageStatus); //TODO:: 여기 setter 추가 해도 되는지 물어보기
        productRepository.save(product);
    }

    public void updateProductStatus() {
        List<Product> products = bidResultRepository.findProductsByDeadlineBeforeAndStorageStatus(LocalDateTime.now(), StorageStatus.SELLING);
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();

            if (product.getBidCnt() == 0) {
                productDTO.setStorageStatus(String.valueOf(StorageStatus.FAIL));
            } else {
                productDTO.setStorageStatus(String.valueOf(StorageStatus.SOLD));
                // bid_cnt가 0이 아닌 경우 storage_status를 'SOLD'로 업데이트
            }

            bidResultRepository.updateProductStatus(product.getStorageStatus(), product.getProductId());
        }
    }



    @Transactional
    @Override
    public void insertBidResultByProduct() {
        List<Product> products = bidResultRepository.findProductsByDeadlineBeforeAndStorageStatus(LocalDateTime.now(), StorageStatus.valueOf("SELLING"));
        for (Product product : products) {
            Bid highestBid = bidResultRepository.findHighestBidByProductId(product.getProductId());

            BidResultDTO bidResultDTO = new BidResultDTO();
            bidResultDTO.setProductId(product.getProductId());
            bidResultDTO.setPaymentStatus(0);
            bidResultDTO.setEndTime(product.getDeadline());
            bidResultDTO.setModTime(LocalDateTime.now());
            bidResultDTO.setCompleteDate(LocalDateTime.now());
            bidResultDTO.setEnrolledTime(LocalDateTime.now());

            if (highestBid == null) {
                bidResultDTO.setCustomerId(null);
                bidResultDTO.setResult(String.valueOf(BidResultStatus.FAILURE));
            } else {
                bidResultDTO.setCustomerId(highestBid.getUser().getSerialNumber());
                bidResultDTO.setResult(String.valueOf(BidResultStatus.SUCCESS));
            }


            BidResult bidResult = new BidResult(
                    product
                    , bidResultDTO.getPaymentStatus()
                    , bidResultDTO.getEndTime()
                    , bidResultDTO.getModTime()
                    , bidResultDTO.getCompleteDate()
                    , bidResultDTO.getEnrolledTime()
                    , bidResultDTO.getCustomerId()
                    , bidResultDTO.getResult()
            );

            bidResultRepository.save(bidResult);
        }
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    public void updateProductStatusAndInsertBidResult() {
        insertBidResultByProduct();
        updateProductStatus();
    }

    @Override
    public List<PendingProductsDTO> getAllPendingAndProcessingWithIdolNameAndPtypeName() {
        List<Object[]> productList = productRepository.findAllPendingAndProcessingWithIdolNameAndPtypeName();
        List<PendingProductsDTO> products = new ArrayList<>();

        for (Object[] objArray : productList) {
            Product product = (Product) objArray[0];
            String idolName = (String) objArray[1];
            PendingProductsDTO productDTO = new PendingProductsDTO();
            productDTO.setProductId(product.getProductId());
            productDTO.setProductName(product.getProductName());
            productDTO.setIdolName(idolName);
            productDTO.setPtypeId(product.getProductType().getPtypeId());
            productDTO.setSerialNumber(product.getUser().getSerialNumber());
            productDTO.setImage1(product.getImage1());
            productDTO.setImage2(product.getImage2());
            productDTO.setProductInfo(product.getProductInfo());
            productDTO.setPrice(product.getPrice());
            productDTO.setPriceUnit(product.getPriceUnit());
            productDTO.setBuyNow(product.getBuyNow());
            productDTO.setTimeLimit(product.getTimeLimit());
            productDTO.setView(product.getView());
            productDTO.setStartPrice(product.getStartPrice());
            productDTO.setRegistrationDate(product.getRegistrationDate());
            productDTO.setBidCnt(product.getBidCnt());
            productDTO.setRequestTime(product.getRequestTime());
            productDTO.setStorageStatus(String.valueOf(product.getStorageStatus()));
            productDTO.setPtypeName(product.getProductType().getPtypeName());

            products.add(productDTO);
        }

        return products;
    }

    @Transactional
    @Override
    public void updateProductStatus(String productId, StorageStatus status) {
        productRepository.updateProductStatus(productId, status);
    }

    @Override
    public int getTodayBidsCount() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        return bidLogRepository.countBidsToday(startOfDay, endOfDay);
    }
}