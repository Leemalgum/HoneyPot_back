package com.beeSpring.beespring.controller.main;

import com.beeSpring.beespring.dto.category.IdolDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.service.main.MainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;


    @GetMapping(path= "/filterCategoryIdolName")
    public List<IdolDTO> showIdolListByCategory(@PathVariable("serialNumber") String serialNumber){

        

        return mainService.showIdolCategoryName("123456789");

    }


//    @GetMapping(path = "/filterByCategory")
//    public ResponseEntity<List<MainProductDTO>> showProductsFilterByCategory(String idolName) {
//        // productId를 이용하여 ProductService를 통해 상품 데이터를 가져옵니다.
//        List<MainProductDTO> productDTO = mainService.filterByCategory(idolName);
//        if (productDTO != null) {
//            // 상품 데이터가 존재할 경우 200 OK 응답과 함께 데이터를 반환합니다.
//            return new ResponseEntity<>(productDTO, HttpStatus.OK);
//        } else {
//            // 상품 데이터가 존재하지 않을 경우 404 Not Found 응답을 반환합니다.
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }



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