package com.beeSpring.beespring.service.main;

import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.category.Idol;
import com.beeSpring.beespring.dto.category.IdolDTO;
import com.beeSpring.beespring.dto.main.MainProductDTO;
import com.beeSpring.beespring.repository.main.MainProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final MainProductRepository mainProductRepository;


    @Override
    public List<IdolDTO> showIdolCategoryName(String serialNumber) {

        List<Object[]> idolList = mainProductRepository.findIdolName(serialNumber);
        List<IdolDTO> idols = new ArrayList<>();

        for(Object[] objArray : idolList) {
            int idolId = (int) objArray[0];
            String idolName = (String) objArray[1];

            IdolDTO idolDTO = new IdolDTO();
            idolDTO.setIdolId(idolId);
            idolDTO.setIdolName(idolName);

            idols.add(idolDTO);
        }

        return idols;
    }

    @Override
    public List<MainProductDTO> filterByCategory(String idolName) {

//        List<String> idolName = productRepository.findIdolName();
//        List<String> idolName = productRepository.findIdolName(String serialNumber);
        Pageable limit = PageRequest.of(0, 4);
        List<Object[]> productList = mainProductRepository.findByCategory(idolName, limit);
        List<MainProductDTO> product = new ArrayList<>();



        for (Object[] objArray : productList) {
            Product products = (Product)objArray[0];
            String idolNames = (String) objArray[1];
            String pTypeName = (String) objArray[2];
            String userId = (String)objArray[3];

            MainProductDTO productDTO = new MainProductDTO();

            productDTO.setUserId(userId);
            productDTO.setIdolName(idolNames);
            productDTO.setPtypeName(pTypeName);

            productDTO.setProductId(products.getProductId());
            productDTO.setProductName(products.getProductName());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setIdolId(products.getIdol().getIdolId());
            productDTO.setPtypeId(products.getProductType().getPtypeId());
            productDTO.setImage1(products.getImage1());
            productDTO.setImage2(products.getImage2());
            productDTO.setImage3(products.getImage3());
            productDTO.setImage4(products.getImage4());
            productDTO.setImage5(products.getImage5());
            productDTO.setSerialNumber(products.getUser().getSerialNumber());
            productDTO.setPriceUnit(products.getPriceUnit());
            productDTO.setStorageStatus(String.valueOf(products.getStorageStatus()));
            productDTO.setRequestTime(products.getRequestTime());
            productDTO.setBidCnt(products.getBidCnt());
            productDTO.setStartPrice(products.getStartPrice());
            productDTO.setPrice(products.getPrice());
            productDTO.setView(products.getView());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setRegistrationDate(products.getRegistrationDate());
            productDTO.setBuyNow(products.getBuyNow());


            product.add(productDTO);
        }

        return product;
    }

    @Override
    public List<MainProductDTO> filterByView() {


        List<Object[]> productList = mainProductRepository.findByView();
        List<MainProductDTO> product = new ArrayList<>();

        for (Object[] objArray : productList) {
            Product products = (Product)objArray[0];
            String idolName = (String) objArray[1];
            String pTypeName = (String) objArray[2];
            String userId = (String)objArray[3];

            MainProductDTO productDTO = new MainProductDTO();

            productDTO.setUserId(userId);
            productDTO.setIdolName(idolName);
            productDTO.setPtypeName(pTypeName);

            productDTO.setProductId(products.getProductId());
            productDTO.setProductName(products.getProductName());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setIdolId(products.getIdol().getIdolId());
            productDTO.setPtypeId(products.getProductType().getPtypeId());
            productDTO.setImage1(products.getImage1());
            productDTO.setImage2(products.getImage2());
            productDTO.setImage3(products.getImage3());
            productDTO.setImage4(products.getImage4());
            productDTO.setImage5(products.getImage5());
            productDTO.setSerialNumber(products.getUser().getSerialNumber());
            productDTO.setPriceUnit(products.getPriceUnit());
            productDTO.setStorageStatus(String.valueOf(products.getStorageStatus()));
            productDTO.setRequestTime(products.getRequestTime());
            productDTO.setBidCnt(products.getBidCnt());
            productDTO.setStartPrice(products.getStartPrice());
            productDTO.setPrice(products.getPrice());
            productDTO.setView(products.getView());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setRegistrationDate(products.getRegistrationDate());
            productDTO.setBuyNow(products.getBuyNow());


            product.add(productDTO);
        }
        return product;
    }

    @Override
    public List<MainProductDTO> filterByLatest() {

        List<Object[]> productList = mainProductRepository.findByLatest();
        List<MainProductDTO> product = new ArrayList<>();

        for (Object[] objArray : productList) {
            Product products = (Product)objArray[0];
            String idolName = (String) objArray[1];
            String pTypeName = (String) objArray[2];
            String userId = (String)objArray[3];

            MainProductDTO productDTO = new MainProductDTO();

            productDTO.setUserId(userId);
            productDTO.setIdolName(idolName);
            productDTO.setPtypeName(pTypeName);

            productDTO.setProductId(products.getProductId());
            productDTO.setProductName(products.getProductName());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setIdolId(products.getIdol().getIdolId());
            productDTO.setPtypeId(products.getProductType().getPtypeId());
            productDTO.setImage1(products.getImage1());
            productDTO.setImage2(products.getImage2());
            productDTO.setImage3(products.getImage3());
            productDTO.setImage4(products.getImage4());
            productDTO.setImage5(products.getImage5());
            productDTO.setSerialNumber(products.getUser().getSerialNumber());
            productDTO.setPriceUnit(products.getPriceUnit());
            productDTO.setStorageStatus(String.valueOf(products.getStorageStatus()));
            productDTO.setRequestTime(products.getRequestTime());
            productDTO.setBidCnt(products.getBidCnt());
            productDTO.setStartPrice(products.getStartPrice());
            productDTO.setPrice(products.getPrice());
            productDTO.setView(products.getView());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setRegistrationDate(products.getRegistrationDate());
            productDTO.setBuyNow(products.getBuyNow());


            product.add(productDTO);
        }
        return product;
    }

    @Override
    public List<MainProductDTO> filterByDeadLine() {
        List<Object[]> productList = mainProductRepository.findByDeadLine();
        List<MainProductDTO> product = new ArrayList<>();


        for (Object[] objArray : productList) {
            Product products = (Product)objArray[0];
            String idolName = (String) objArray[1];
            String pTypeName = (String) objArray[2];
            String userId = (String)objArray[3];

            MainProductDTO productDTO = new MainProductDTO();

            productDTO.setUserId(userId);
            productDTO.setIdolName(idolName);
            productDTO.setPtypeName(pTypeName);

            productDTO.setProductId(products.getProductId());
            productDTO.setProductName(products.getProductName());
            productDTO.setIdolId(products.getIdol().getIdolId());
            productDTO.setPtypeId(products.getProductType().getPtypeId());
            productDTO.setImage1(products.getImage1());
            productDTO.setImage2(products.getImage2());
            productDTO.setImage3(products.getImage3());
            productDTO.setImage4(products.getImage4());
            productDTO.setImage5(products.getImage5());
            productDTO.setSerialNumber(products.getUser().getSerialNumber());
            productDTO.setPriceUnit(products.getPriceUnit());
            productDTO.setStorageStatus(String.valueOf(products.getStorageStatus()));
            productDTO.setRequestTime(products.getRequestTime());
            productDTO.setBidCnt(products.getBidCnt());
            productDTO.setStartPrice(products.getStartPrice());
            productDTO.setPrice(products.getPrice());
            productDTO.setView(products.getView());
            productDTO.setTimeLimit(products.getTimeLimit());
            productDTO.setRegistrationDate(products.getRegistrationDate());
            productDTO.setBuyNow(products.getBuyNow());


            product.add(productDTO);
        }
        return product;
    }

//    @Autowired
//    public List<MainProductDTO> getActiveEntities() {
//        List<Product> mainProductDTO = mainProductRepository.findAll();
//        return mainProductDTO.stream()
//                .map(entity -> {
//                    LocalDateTime deadline = entity.getRegistrationDate().plusHours(entity.getTimeLimit());
//                    return new MainProductDTO(entity.getProductId(), entity.getRegistrationDate(), entity.getTimeLimit(), deadline);
//                })
//                .collect(Collectors.toList());
//    }
// 1. 테이블에 데드라인 컬럼 추가하기
// 2. RegistrationTime이 찍혀서 테이블에 저장되면 Timlimit이랑 합쳐서 데드라인 저장되는 트리거 작성
// 3. 테이블에 저장된 데드라인 내림차순 정렬해서 (상태: 진행중) 화면에 출력

//4. 마감시간 - 현재 시간 = 0 이면 경매결과 트리거 작동되도록


}