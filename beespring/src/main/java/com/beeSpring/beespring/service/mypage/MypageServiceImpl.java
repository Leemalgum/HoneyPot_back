package com.beeSpring.beespring.service.mypage;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.domain.shipping.DeliveryStatus;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.domain.user.UserIdol;
import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.AddressDTO;
import com.beeSpring.beespring.dto.mypage.PaymentProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.dto.mypage.UserProfileDTO;
import com.beeSpring.beespring.dto.user.UserDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import com.beeSpring.beespring.repository.category.IdolRepository;
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
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageServiceImpl implements MypageService {
    private static final Logger logger = LoggerFactory.getLogger(MypageServiceImpl.class);
    private final ProductRepository productRepository;
    private final AmazonS3 s3Client;
    private final UserRepository userRepository;
    private final UserIdolRepository userIdolRepository;
    private final IdolRepository idolRepository;


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
                    .paymentStatus((Integer) objArray[1])
                    .completeDate((LocalDateTime) objArray[2])
                    .deliveryStatus((DeliveryStatus) objArray[3])
                    .serialNumber((String) objArray[4])
                    .productName((String) objArray[5])
                    .image1((String) objArray[6])
                    .priceUnit((Integer) objArray[7])
                    .startPrice((Integer) objArray[8])
                    .bidCnt((Integer) objArray[9])
                    .nickName((String) objArray[10])
                    .storageStatus((StorageStatus) objArray[11])
                    .build();

            if (objArray[3] != null) {
                // 배송 상태가 null이 아닌 경우에만 toString() 호출하여 문자열로 변환
                productDTO.setDeliveryStatus(((DeliveryStatus) objArray[3]));
            } else {
                productDTO.setDeliveryStatus(DeliveryStatus.valueOf("NULL"));
            }

//            logger.info("product: {}", productDTO);
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

    @Override
    public PaymentProductDTO getProductById(String serialNumber, String productId) {
        List<Object[]> productData = productRepository.findByProductId(serialNumber, productId);

        if (productData != null) {
            try {
                // AddressDTO 리스트 생성
                List<AddressDTO> addresses = new ArrayList<>();
                for (Object[] productDatum : productData) {
                    logger.info("for");
                    logger.info("{}", productDatum);
                    addresses.add(
                            new AddressDTO((Long) productDatum[6], (String) productDatum[7], (String) productDatum[8], (String) productDatum[9], (String) productDatum[10], (String) productDatum[11], (String) productDatum[12]));
                }

                return PaymentProductDTO.builder()
                        .productId(productId)
                        .serialNumber(serialNumber)
                        .productName((String) productData.getFirst()[2])
                        .priceUnit((Integer) productData.getFirst()[3])
                        .startPrice((Integer) productData.getFirst()[4])
                        .bidCnt((Integer) productData.getFirst()[5])
                        .address(addresses)
                        .build();
            } catch (Exception e) {
                logger.error("Error building productDTO", e);
                return null;
            }
        } else {
            logger.info("No product data found for serialNumber: {} and productId: {}", serialNumber, productId);
            return null;
        }
    }

    @Transactional
    @Override
    public void startSale(String productId) {
        logger.info("startsale method called");
        // 제품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        // 판매 상태 변경
        product.updateStorageStatus(StorageStatus.valueOf("SELLING"));

        // 변경된 상태를 저장
        productRepository.save(product);
    }

    @Override
    public List<ProductWithSerialNumberDTO> getPurchaseListBySerialNumber(String serialNumber) {
        List<Object[]> productList = productRepository.findByCustomerId(serialNumber);
        List<ProductWithSerialNumberDTO> products = new ArrayList<>();
        logger.info("-------getPurchaseListBySerialNumber-------");

//        logger.info("Retrieved product list:");
//        for (Object[] objArray : productList) {
//            StringBuilder stringBuilder = new StringBuilder();
//            for (Object obj : objArray) {
//                stringBuilder.append(obj).append(", ");
//            }
//            logger.info(stringBuilder.toString());
//        }
        for (Object[] objArray : productList) {
            ProductWithSerialNumberDTO productDTO = ProductWithSerialNumberDTO.builder()
                    .productId((String) objArray[0])
                    .paymentStatus((Integer) objArray[1])
                    .completeDate((LocalDateTime) objArray[2])
                    .deliveryStatus((DeliveryStatus) objArray[3])
                    .serialNumber((String) objArray[4])
                    .productName((String) objArray[5])
                    .image1((String) objArray[6])
                    .priceUnit((Integer) objArray[7])
                    .startPrice((Integer) objArray[8])
                    .bidCnt((Integer) objArray[9])
                    .nickName((String) objArray[10])
                    .build();

//            if (objArray[3] != null) {
//                // 배송 상태가 null이 아닌 경우에만 toString() 호출하여 문자열로 변환
//                productDTO.setDeliveryStatus(((DeliveryStatus) objArray[3]));
//            } else {
//                productDTO.setDeliveryStatus(DeliveryStatus.valueOf("NULL"));
//            }

            logger.info("product: {}", productDTO);
            products.add(productDTO);
        }

        return products;

    }


    //프로필 파트
    @Override
    public UserProfileDTO getProfile(String serialNumber) {
        User user = userRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> idolNames = userRepository.findIdolNamesByUserSerialNumber(serialNumber);

        return UserProfileDTO.builder()
                .profileImage(user.getProfileImage())
                .nickname(user.getNickname())
                .userAccount(user.getUserAccount())
                .userId(user.getUserId())
                .idolNames(idolNames)
                .build();

    }

    @Override
    @Transactional
    public void updateProfile(String serialNumber, UserProfileDTO userProfileDTO, MultipartFile profileImageFile) throws IOException {
        User user = userRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new RuntimeException("User not found"));

        logger.info(String.valueOf(user));
        user.setNickname(userProfileDTO.getNickname());
        user.setUserAccount(userProfileDTO.getUserAccount());
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            String profileImageUrl = storeImage(profileImageFile);
            user.setProfileImage(profileImageUrl);
        }

        userRepository.save(user);

        // 업데이트된 아이돌 리스트를 처리
        userIdolRepository.deleteByUser(user);
        List<String> idolNames = userProfileDTO.getIdolNames();
        if (idolNames != null && !idolNames.isEmpty()) {
            for (String idolName : idolNames) {
                Integer idolId = idolRepository.findIdByName(idolName);
                if (idolId != null) {
//                    userIdolRepository.insertUserIdol(serialNumber, idolId);
                    UserIdol userIdol = UserIdol.builder()
                            .user(user)
                            .idol(idolRepository.getReferenceById(idolId))
                            .build();
                    userIdolRepository.save(userIdol);
                }
            }
        }
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
