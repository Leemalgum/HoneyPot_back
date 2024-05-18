package com.beeSpring.beespring.controller.main;

import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.service.main.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;


    @GetMapping(path= "/filterByCategory")
    public List<MainProductDTO> showProductsFilterByCategory(){
        return mainService.filterByCategory();
    }

    @GetMapping(path = "/filterByView")
    public List<MainProductDTO> showProductsFilterByView(){
        return mainService.filterByView();
    }

    @GetMapping(path = "/filterByLatest")
    public List<MainProductDTO> showProductsFilterByLatest(){

        return mainService.filterByLatest();
    }

    @GetMapping(path = "/filterByDeadLine")
    public List<MainProductDTO> showProductsFilterByDeadline(){
        return mainService.filterByDeadLine();
    }


//    @GetMapping(path = "/")
//    public String showIndex(Model model) {
//        List<MainProductDTO> productsByCategory = mainService.filterByCategory();
//        List<MainProductDTO> productsByView = mainService.filterByView();
//        List<MainProductDTO> productsByLatest = mainService.filterByLatest();
//
//        model.addAttribute("productsByCategory", productsByCategory);
//        model.addAttribute("productsByView", productsByView);
//        model.addAttribute("productsByLatest", productsByLatest);
//
//        return "index";
//    }


}