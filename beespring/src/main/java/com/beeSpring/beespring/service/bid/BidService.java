package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;

import java.util.List;


public interface BidService {

    public List<ProductWithIdolNameDTO> getAllProductsWithIdolName();

    public MainProductDTO getProductById(String productId);

    public String getUserIdByProductId(String productId);
}