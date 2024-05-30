package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.domain.bid.BidResult;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.dto.bid.PendingProductsDTO;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;

import java.util.List;


public interface BidService {

    List<ProductWithIdolNameDTO> getAllProductsWithIdolName();

    MainProductDTO getProductById(String productId);

    String getUserIdByProductId(String productId);

    Product getProductEntityById(String productId);

    void increaseViewCount(String productId);

    void updateProductStatusAfterConfirm(String productId, String storageStatus);

    void insertBidResultByProduct();
    void  updateProductStatus();

    List<PendingProductsDTO> getAllPendingAndProcessingWithIdolNameAndPtypeName();

    void updateProductStatus(String productId, StorageStatus status);

    int getTodayBidsCount();
}