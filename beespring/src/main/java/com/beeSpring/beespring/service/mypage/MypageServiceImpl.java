package com.beeSpring.beespring.service.mypage;

import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor

public class MypageServiceImpl implements MypageService {
    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
    private final ProductRepository productRepository;

    @Override
    public List<ProductWithSerialNumberDTO> getProductListBySerialNumber(String serialNumber) {
        List<Object[]> productList = productRepository.findBySerialNumber(serialNumber);
        List<ProductWithSerialNumberDTO> products = new ArrayList<>();

        logger.info("Retrieved product list:");
        for (Object[] objArray : productList) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Object obj : objArray) {
                stringBuilder.append(obj).append(", ");
            }
            logger.info(stringBuilder.toString());
        }
        for (Object[] objArray : productList) {
            ProductWithSerialNumberDTO productDTO = ProductWithSerialNumberDTO.builder()
                    .productId((String) objArray[0])
                    .paymentStatus((int) objArray[1])
                    .deliveryStatus((DeliveryStatus) objArray[2])
                    .serialNumber((String) objArray[3])
                    .productName((String) objArray[4])
                    .image1((String) objArray[5])
                    .priceUnit((Integer) objArray[6])
                    .startPrice((int) objArray[7])
                    .bidCnt((int) objArray[8])
                    .build();

            products.add(productDTO);
        }

        return products;
    }
}
