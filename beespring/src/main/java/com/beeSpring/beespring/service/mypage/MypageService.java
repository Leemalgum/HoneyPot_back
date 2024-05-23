package com.beeSpring.beespring.service.mypage;

import com.beeSpring.beespring.dto.bid.ProductDTO;
import com.beeSpring.beespring.dto.mypage.PaymentProductDTO;
import com.beeSpring.beespring.dto.mypage.ProductWithSerialNumberDTO;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.dto.user.UserDTO;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface MypageService {

    List<ProductWithSerialNumberDTO> getProductListBySerialNumber(String serialNumber);

    String storeImage(MultipartFile file) throws IOException;
    String storeProfileImage(MultipartFile file) throws IOException;

    void registerProduct(ProductDTO productDTO);
    UserDTO getProfile(String serialNumber);
    void saveProfile(String serialNumber, UserDTO userDTO, MultipartFile profileImageFile) throws IOException;

    PaymentProductDTO getProductById(String serialNumber, String productId);

    void startSale(String productId);
}
