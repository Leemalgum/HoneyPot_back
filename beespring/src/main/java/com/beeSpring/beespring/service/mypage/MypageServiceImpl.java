package com.beeSpring.beespring.service.mypage;

import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
    private final ProductRepository productRepository;
    private final AmazonS3 s3Client;


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

    @Override
    public String storeImage(MultipartFile file) throws IOException {
        String objectName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest putRequest = new PutObjectRequest("beespring-bucket", objectName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
            logger.info("File uploaded successfully to S3. URL: {}", s3Client.getUrl("beespring-bucket", objectName).toString());
            return s3Client.getUrl("beespring-bucket", objectName).toString();
        } catch (AmazonServiceException e) {
            logger.error("Failed to upload file to S3. AWS error message: {}", e.getErrorMessage());
            throw new IOException("Failed to upload file to S3.", e);
        } catch (SdkClientException e) {
            logger.error("Failed to upload file to S3. SDK error message: {}", e.getMessage());
            throw new IOException("Failed to upload file to S3.", e);
        }
    }

    @Override
    @Transactional
    public void registerProduct(ProductDTO productDTO) {
        productRepository.save(productDTO.toEntity());
    }
}
