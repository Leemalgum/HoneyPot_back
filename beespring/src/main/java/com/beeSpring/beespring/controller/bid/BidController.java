package com.beeSpring.beespring.controller.bid;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.service.bid.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService){
        this.bidService = bidService;
    }

    @GetMapping(path = "/products")
    public List<Product> getAllProducts(){
        return bidService.getAllProducts();
    }

}
