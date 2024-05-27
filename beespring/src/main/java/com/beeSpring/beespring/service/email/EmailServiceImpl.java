package com.beeSpring.beespring.service.email;

import com.beeSpring.beespring.repository.bid.BidLogRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "meee22223@gmail.com";
    private final BidLogRepository bidLogRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    /**
     * 경매 종료 1시간에 전에 보낼 메일 작성 메서드
     *
     * @param nickname
     * @param email
     * @param productName
     * @return
     */
    @Override
    public MimeMessage createMail(String nickname, String email, String productName, String productId) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
//            message.setFrom(senderEmail);   // 보내는 이메일
//            message.setRecipients(MimeMessage.RecipientType.TO, email); // 보낼 이메일 설정
//            message.setSubject("경매 종료 임박 알림");  // 제목 설정
//            String body = "";
//            body += STR."<h1>안녕하세요 \{nickname}님</h1>";
//            body += STR."<div>\{productName} 경매가 한 시간 이내에 종료됩니다.\n</div>";
//            body += "<div>" + "마지막 입찰 기회를 놓치지 마세요!<br>" +
//                    "감사합니다.<br>" +
//                    "꿀단지 팀" + "</div>";
//            message.setText(body, "UTF-8", "html");
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(email);
            helper.setSubject("경매 종료 임박 알림");

            String body = "<h1>안녕하세요 " + nickname + "님</h1>";
            body += "<div><a href='http://localhost:3000/bid-details/" + productId + "'>" + productName + "</a> 경매가 한 시간 이내에 종료됩니다.</div>";
            body += "<div><img src='cid:logoImage' alt='Logo Image' style='width:200px;'></div>";
            body += "<div>마지막 입찰 기회를 놓치지 마세요!<br>감사합니다.<br>꿀단지 팀</div>";

            helper.setText(body, true); // true를 설정하여 HTML을 사용하도록 함

            // 로고 이미지 첨부
            ClassPathResource resource = new ClassPathResource("static/logo.png"); // 로고 이미지의 경로를 설정
            helper.addInline("logoImage", resource);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     * 경매 종료 시간까지 1시간 남은 상품들에 입찰을 한 사용자 정보를 가져와서 메일 전송
     */
//    @Scheduled(fixedRate = 3600000)  // Schedules the method to run every hour
    public List<Object[]> sendBidClosingReminderEmail(String productId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneHourLater = now.plusHours(1);

        List<Object[]> usersWithProductDeadline = bidLogRepository.findUsersWithProductDeadlineWithinAnHour(now, oneHourLater);

        logger.info("Query Result: " + usersWithProductDeadline.size() + " records found");

        for (Object[] userData : usersWithProductDeadline) {
            String nickname = (String) userData[0];
            String email = (String) userData[1];
            String productName = (String) userData[2];

            logger.info("Nickname: {}, Email: {}, Product Name: {}, Product Image: {}", nickname, email, productName);

            MimeMessage message = createMail(nickname, email, productName,  productId);
            javaMailSender.send(message);
        }
        return usersWithProductDeadline;
    }

}
