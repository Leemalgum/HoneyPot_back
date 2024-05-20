package com.beeSpring.beespring.service.main;

import com.beeSpring.beespring.dto.category.IdolDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;


import java.util.Date;
import java.util.List;


public interface MainService {

    public List<IdolDTO> showIdolCategoryName(String serialNumber);

    public List<MainProductDTO> filterByCategory(String idolName);

    public List<MainProductDTO> filterByView();

    public List<MainProductDTO> filterByLatest();

    public List<MainProductDTO> filterByDeadLine();

}