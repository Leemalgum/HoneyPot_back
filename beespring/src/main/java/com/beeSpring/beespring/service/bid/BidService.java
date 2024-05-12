package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidService {
    private final ProductRepository productRepository;

    @Autowired
    public BidService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }
}
