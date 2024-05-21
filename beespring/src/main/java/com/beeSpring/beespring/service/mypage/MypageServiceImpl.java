package com.beeSpring.beespring.service.mypage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.PaymentProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.user.UserDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import com.beeSpring.beespring.repository.user.UserIdolRepository;
import com.beeSpring.beespring.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
    private final ProductRepository productRepository;
    private final AmazonS3 s3Client;
    private final UserRepository userRepository;
    private final UserIdolRepository userIdolRepository;


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
                    .completeDate((LocalDateTime) objArray[2])
                    .deliveryStatus((DeliveryStatus) objArray[3])
                    .serialNumber((String) objArray[4])
                    .productName((String) objArray[5])
                    .image1((String) objArray[6])
                    .priceUnit((Integer) objArray[7])
                    .startPrice((int) objArray[8])
                    .bidCnt((int) objArray[9])
                    .nickName((String) objArray[10])
                    .build();

            products.add(productDTO);
        }

        return products;
    }

    //입고요청파트
    @Override
    public String storeImage(MultipartFile file) throws IOException {
        String objectName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {

            PutObjectRequest putRequest = new PutObjectRequest("beespring-bucket/requestImages", objectName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
            logger.info("File uploaded successfully to S3. URL: {}", s3Client.getUrl("beespring-bucket/requestImages", objectName).toString());
            return s3Client.getUrl("beespring-bucket/requestImages", objectName).toString();
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

    public PaymentProductDTO getProductById(String serialNumber, String productId) {
        List<Object[]> productDataList = productRepository.findByProductId(serialNumber, productId);
        Object[] productData = productDataList.get(0);

        if (productData != null) {
            try {
                PaymentProductDTO productDTO = PaymentProductDTO.builder()
                        .productId(productId)
                        .serialNumber(serialNumber)
                        .productName((String) productData[2])
                        .priceUnit((Integer) productData[3])
                        .startPrice((Integer) productData[4])
                        .bidCnt((Integer) productData[5])
                        .recipientName((String) productData[6])
                        .postCode((String) productData[7])
                        .roadAddress((String) productData[8])
                        .detailAddress((String) productData[9])
                        .recipientPhone((String) productData[10])
                        .build();
                logger.info("service: {}", productDTO);
                return productDTO;
            } catch (Exception e) {
                logger.error("Error building productDTO", e);
                return null;
            }
        } else {
            logger.info("null");
            return null;
        }
    }

    //프로필 파트
    @Override
    public UserDTO getProfile(String serialNumber) {
        User user = userRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new UserDTO(
                user.getSerialNumber(),
                user.getUserId(),
                user.getRoleId(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getNickname(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getRegistrationDate(),
                user.getBirthdate(),
                user.getReportedCnt(),
                user.getState(),
                user.getReason(),
                user.getSuspended(),
                user.getModDate(),
                user.getRefreshToken(),
                user.getProfileImage(),
                user.getTag1(),
                user.getTag2(),
                user.getTag3()
        );
    }

    @Override
    @Transactional
    public void saveProfile(String serialNumber, UserDTO userDTO, MultipartFile profileImageFile) throws IOException {
        User user = userRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String profileImageUrl = storeProfileImage(profileImageFile);
            user.setProfileImage(profileImageUrl);
        }

        user.setNickname(userDTO.getNickname());
        user.setTag1(userDTO.getTag1());
        user.setTag2(userDTO.getTag2());
        user.setTag3(userDTO.getTag3());

        userRepository.save(user);
    }

    @Override
    public String storeProfileImage(MultipartFile file) throws IOException {
        String objectName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            PutObjectRequest putRequest = new PutObjectRequest("beespring-bucket/profile", objectName, file.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);
            s3Client.putObject(putRequest);
            logger.info("File uploaded successfully to S3. URL: {}", s3Client.getUrl("beespring-bucket", objectName).toString());
            return s3Client.getUrl("beespring-bucket/profile", objectName).toString();
        } catch (AmazonServiceException e) {
            logger.error("Failed to upload file to S3. AWS error message: {}", e.getErrorMessage());
            throw new IOException("Failed to upload file to S3.", e);
        } catch (SdkClientException e) {
            logger.error("Failed to upload file to S3. SDK error message: {}", e.getMessage());
            throw new IOException("Failed to upload file to S3.", e);
        }
    }
}
