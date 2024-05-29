package com.beeSpring.beespring.controller.admin;

import com.beeSpring.beespring.domain.bid.StorageStatus;
import com.beeSpring.beespring.domain.user.State;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.dto.admin.DeclineReasonDTO;
import com.beeSpring.beespring.dto.bid.PendingProductsDTO;
import com.beeSpring.beespring.dto.user.ForgotPasswordRequest;
import com.beeSpring.beespring.dto.user.ManageUserProjection;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.service.bid.BidService;
import com.beeSpring.beespring.service.email.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(com.beeSpring.beespring.controller.admin.AdminController.class);
    private final BidService bidService;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @GetMapping(path = "/pending-processing-products")
    public List<PendingProductsDTO> getAllPendingAndProcessingWithIdolNameAndPtypeName(){
        return bidService.getAllPendingAndProcessingWithIdolNameAndPtypeName();
    }

    @GetMapping("/get-users")
    public ResponseEntity<List<ManageUserProjection>> getAllUsers() {
        List<ManageUserProjection> users = userRepository.findAllUsersAsDTO();
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PostMapping("/approve-product")
    public ResponseEntity<String> approveProduct(@RequestBody PendingProductsDTO pendingProductsDTO) {
        System.out.println("시리얼 넘버" + pendingProductsDTO.getSerialNumber());
        System.out.println("상태 : " + pendingProductsDTO.getStorageStatus());
        System.out.println("상품 아이디 : " + pendingProductsDTO.getProductId());

        try {
            bidService.updateProductStatus(pendingProductsDTO.getProductId(), StorageStatus.PROCESSING);
            Optional<User> user = userRepository.findBySerialNumber(pendingProductsDTO.getSerialNumber());
            if (user.isPresent()) {
                emailService.sendReceiptApprovementEmail(user.get().getEmail(), pendingProductsDTO);
                return ResponseEntity.ok("Product approved successfully and email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            logger.error("Error approving product", e);
            return ResponseEntity.status(500).body("Error approving product.");
        }
    }

    @Transactional
    @PostMapping("/final-approve-product")
    public ResponseEntity<String> finalApproveProduct(@RequestBody PendingProductsDTO pendingProductsDTO) {
        try {
            bidService.updateProductStatus(pendingProductsDTO.getProductId(), StorageStatus.READY);
            Optional<User> user = userRepository.findBySerialNumber(pendingProductsDTO.getSerialNumber());
            if (user.isPresent()) {
                emailService.sendRegisterApprovementEmail(user.get().getEmail(), pendingProductsDTO);
                return ResponseEntity.ok("Product finally approved successfully and email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            logger.error("Error finally approving product", e);
            return ResponseEntity.status(500).body("Error finally approving product.");
        }
    }

    @Transactional
    @PostMapping("/decline-product")
    public ResponseEntity<String> declineProduct(@RequestBody DeclineReasonDTO declineReasonDTO) {
        try {
            bidService.updateProductStatus(declineReasonDTO.getProductId(), StorageStatus.DECLINED);
            Optional<User> user = userRepository.findBySerialNumber(declineReasonDTO.getSerialNumber());
            if (user.isPresent()) {
                emailService.sendReceiptRejectionEmail(user.get().getEmail(), declineReasonDTO);
                return ResponseEntity.ok("Product declined successfully and email sent.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }
        } catch (Exception e) {
            logger.error("Error declining product", e);
            return ResponseEntity.status(500).body("Error declining product.");
        }
    }

    @Transactional
    @PostMapping("/user-state")
    public ResponseEntity<String> updateUserState(@RequestBody Map<String, String> payload) {
        try {
            String userId = payload.get("userId");
            String state = payload.get("state");
            userRepository.updateUserState(userId, State.valueOf(state));
            return ResponseEntity.ok("회원 상태가 변경되었습니다.");
        } catch (Exception e) {
            logger.error("회원 상태 변경 중 에러가 발생했습니다.", e);
            return ResponseEntity.status(500).body("서버 에러가 발생했습니다.");
        }
    }

    @Transactional
    @GetMapping("/bids/today-count")
    public int getTodayBidsCount() {
        return bidService.getTodayBidsCount();
    }

}