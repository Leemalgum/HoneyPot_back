package com.beeSpring.beespring.service.main;

import com.beeSpring.beespring.dto.main.MainProductDTO;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


public interface MainService {


    public List<MainProductDTO> filterByCategory();

    public List<MainProductDTO> filterByView();

    public List<MainProductDTO> filterByLatest();

    public List<MainProductDTO> filterByDeadLine();


    public String getUserIdByProductId(String productId);
}