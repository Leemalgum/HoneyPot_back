package com.beeSpring.beespring.controller.user;

import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.user.UserRepository;
import com.beeSpring.beespring.response.CustomApiResponse;
import com.beeSpring.beespring.response.ResponseCode;
import com.beeSpring.beespring.security.jwt.JwtTokenProvider;
import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Balance;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.model.MessageStatusType;
import net.nurigo.sdk.message.model.StorageType;
import net.nurigo.sdk.message.request.MessageListRequest;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.MessageListResponse;
import net.nurigo.sdk.message.response.MultipleDetailMessageSentResponse;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/mms")
public class MobileController {
    private static final Logger log = LoggerFactory.getLogger(MobileController.class);
    private DefaultMessageService messageService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    public MobileController(UserRepository userRepository, @Lazy JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostConstruct
    public void init() {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
    }

    /**
     * 메시지 조회
     */
    @GetMapping("/get-message-list")
    public MessageListResponse getMessageList() {
        MessageListRequest request = new MessageListRequest();

         request.setLimit(1);

         request.setStartKey("메시지 ID");

         request.setTo("검색할 수신번호");
         request.setFrom("검색할 발신번호");

         // 메시지 상태 검색, PENDING은 대기 건, SENDING은 발송 중,COMPLETE는 발송완료, FAILED는 발송에 실패한 모든 건입니다.
        request.setStatus(MessageStatusType.PENDING);
        request.setStatus(MessageStatusType.SENDING);
        request.setStatus(MessageStatusType.COMPLETE);
        request.setStatus(MessageStatusType.FAILED);

        request.setMessageId("검색할 메시지 ID");

        // 검색할 메시지 목록
        ArrayList<String> messageIds = new ArrayList<>();
        messageIds.add("검색할 메시지 ID");
        request.setMessageIds(messageIds);

        // 조회 할 메시지 유형 검색
        // SMS: 단문
        // LMS: 장문
        // MMS: 사진문자
        // ATA: 알림톡
        // CTA: 친구톡
        // CTI: 이미지 친구톡
        // NSA: 네이버 스마트알림
        // RCS_SMS: RCS 단문
        // RCS_LMS: RCS 장문
        // RCS_MMS: RCS 사진문자
        // RCS_TPL: RCS 템플릿문자
        // request.setType("조회 할 메시지 유형");

        return this.messageService.getMessageList(request);
    }

    /**
     * 아이디 찾기 시 단일 메시지 발송
     */
    @PostMapping("/send-one")
    public ResponseEntity<?> sendOne(@RequestParam("name") String name,
                                     @RequestParam("mobileNumber") String mobileNumber) {
        System.out.println(name);
        System.out.println(mobileNumber);

        try {
            Optional<User> userOptional = userRepository.findByFirstNameAndMobileNumber(name, mobileNumber);

            if (userOptional != null && userOptional.isPresent()) {
                Message message = new Message();

                message.setFrom("01041147085");
                message.setTo(mobileNumber);

                String authCode = generateRandomAuthCode();

                message.setText("꿀단지 인증번호는 [" + authCode + "] 입니다.");

                /*SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
                System.out.println(response);*/

                try {
                    // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
                    messageService.send(message);
                } catch (NurigoMessageNotReceivedException exception) {
                    // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
                    System.out.println(exception.getFailedMessageList());
                    System.out.println(exception.getMessage());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }

                User user = userOptional.get();
                String userId = user.getUserId();
                String partialUserId = userId.length() > 3 ? userId.substring(0, 3) + "*****" : userId;

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("authCode", authCode);
                responseMap.put("userId", partialUserId);
                responseMap.put("message", "인증번호가 발송되었습니다.");

                return ResponseEntity.ok(CustomApiResponse.success(responseMap, ResponseCode.SEND_MMS_SUCCESS.getMessage()));
            } else {
                log.warn("User not found with name: {} and mobile number: {}", name, mobileNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("서버 오류");
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 회원 가입 시 단일 메시지 발송
     */
    @PostMapping("/send-one-signup")
    public ResponseEntity<?> sendOneSignup(@RequestParam("name") String name,
                                     @RequestParam("mobileNumber") String mobileNumber) {
        System.out.println(name);
        System.out.println(mobileNumber);

        try {
            Optional<User> userOptional = userRepository.findByFirstNameAndMobileNumber(name, mobileNumber);

            if (!userOptional.isPresent()) {
                Message message = new Message();

                message.setFrom("01041147085");
                message.setTo(mobileNumber);

                String authCode = generateRandomAuthCode();

                message.setText("꿀단지 인증번호는 [" + authCode + "] 입니다.");

                /*SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
                System.out.println(response);*/

                try {
                    // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
                    messageService.send(message);
                } catch (NurigoMessageNotReceivedException exception) {
                    // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
                    System.out.println(exception.getFailedMessageList());
                    System.out.println(exception.getMessage());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }

                Map<String, Object> responseMap = new HashMap<>();
                responseMap.put("authCode", authCode);
                responseMap.put("message", "인증번호가 발송되었습니다.");

                return ResponseEntity.ok(CustomApiResponse.success(responseMap, ResponseCode.SEND_MMS_SUCCESS.getMessage()));
            } else {
                log.warn("User found with name: {} and mobile number: {}", name, mobileNumber);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            System.out.println("서버 오류");
            log.error("Unexpected error", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * MMS 발송 예제
     * 단일 발송, 여러 건 발송 상관없이 이용 가능
     */
    @PostMapping("/send-mms")
    public SingleMessageSentResponse sendMmsByResourcePath() throws IOException {
        ClassPathResource resource = new ClassPathResource("static/sample.jpg");
        File file = resource.getFile();
        String imageId = this.messageService.uploadFile(file, StorageType.MMS, null);

        Message message = new Message();

        message.setFrom("발신번호 입력");
        message.setTo("수신번호 입력");
        message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다.");
        message.setImageId(imageId);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }

    /**
     * 여러 메시지 발송 예제
     * 한 번 실행으로 최대 10,000건 까지의 메시지가 발송 가능합니다.
     */
    @PostMapping("/send-many")
    public MultipleDetailMessageSentResponse sendMany() {
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Message message = new Message();

            message.setFrom("발신번호 입력");
            message.setTo("수신번호 입력");
            message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다." + i);

            // 메시지 건건 마다 사용자가 원하는 커스텀 값(특정 주문/결제 건의 ID를 넣는등)을 map 형태로 기입하여 전송 후 확인해볼 수 있습니다
            HashMap<String, String> map = new HashMap<>();

            map.put("키 입력", "값 입력");
            message.setCustomFields(map);

            messageList.add(message);
        }

        try {
            // send 메소드로 단일 Message 객체를 넣어도 동작
            // 세 번째 파라미터인 showMessageList 값을 true로 설정할 경우 MultipleDetailMessageSentResponse에서 MessageList를 리턴
            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, false, true);

            // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체
            //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, true);

            System.out.println(response);

            return response;
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }


    @PostMapping("/send-scheduled-messages")
    public MultipleDetailMessageSentResponse sendScheduledMessages() {
        ArrayList<Message> messageList = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            Message message = new Message();

            message.setFrom("발신번호 입력");
            message.setTo("수신번호 입력");
            message.setText("한글 45자, 영자 90자 이하 입력되면 자동으로 SMS타입의 메시지가 추가됩니다." + i);

            messageList.add(message);
        }

        try {
            LocalDateTime localDateTime = LocalDateTime.parse("2022-11-26 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ZoneOffset zoneOffset = ZoneId.systemDefault().getRules().getOffset(localDateTime);
            Instant instant = localDateTime.toInstant(zoneOffset);

            MultipleDetailMessageSentResponse response = this.messageService.send(messageList, instant);

            // 중복 수신번호를 허용하고 싶으실 경우 위 코드 대신 아래코드로 대체해 사용
            //MultipleDetailMessageSentResponse response = this.messageService.send(messageList, instant, true);

            System.out.println(response);

            return response;
        } catch (NurigoMessageNotReceivedException exception) {
            System.out.println(exception.getFailedMessageList());
            System.out.println(exception.getMessage());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    /**
     * 잔액 조회 예제
     */
    @GetMapping("/get-balance")
    public Balance getBalance() {
        Balance balance = this.messageService.getBalance();
        System.out.println(balance);

        return balance;
    }

    private String generateRandomAuthCode() {
        Random random = new Random();
        int authCode = 100000 + random.nextInt(900000);
        return String.valueOf(authCode);
    }
}
