package com.beeSpring.beespring;

import com.beeSpring.beespring.service.mypage.MypageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class MypageServiceTests {

    @Autowired
    private MypageService mypageService;

    @Test
    public void testStoreImage() throws IOException {
        // 파일을 로컬 시스템에서 읽습니다. 적절한 파일 경로를 지정하세요.
        File file = new File("src/main/resources/단지곰.png");
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file",
                file.getName(), "image/png", input);

        // 서비스 메소드 호출
        String returnedUrl = mypageService.storeImage(multipartFile);

        // 반환된 URL을 검증합니다.
        assertThat(returnedUrl).isNotNull();
//        assertThat(returnedUrl).contains("s3.amazonaws.com");

        // 파일 스트림을 닫습니다.
        input.close();
    }
}