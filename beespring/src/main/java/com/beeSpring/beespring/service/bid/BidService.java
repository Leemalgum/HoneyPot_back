package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.bid.ProductWithIdolNameDTO;

import java.util.List;


public interface BidService {

    public List<ProductWithIdolNameDTO> getAllProductsWithIdolName();

    public ProductDTO getProductById(String productId);
}
