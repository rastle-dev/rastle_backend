package rastle.dev.rastle_backend.domain.member.application;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.SignUpDto;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@Slf4j
@Transactional
@ActiveProfiles("test")
@SpringBootTest
class MemberAuthServiceTest {
    @Autowired
    private MemberAuthService memberAuthService;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        memberRepository.deleteAllByEmail("duplicateEmail@email.com");
    }

    @AfterEach
    void afterEach() {
        memberRepository.deleteAllByEmail("duplicateEmail@email.com");
    }

    @Test
    @DisplayName("회원가입 동시 요청 실패 테스트")
    public void duplicateSignUpTest() throws Exception{
        // given

        memberRepository.deleteAllByEmail("duplicateEmail@email.com");
        int threadCount = 2;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        SignUpDto duplicateUser = SignUpDto.builder()
            .email("duplicateEmail@email.com")
            .password("randomPwd!!2")
            .username("duplicateUser")
            .phoneNumber("01012341234")
            .build();
        // when
        IntStream.range(0, threadCount).forEach((i) -> {
            executorService.submit(() -> {
                try {
                    memberAuthService.signUp(duplicateUser);
                } finally {
                    latch.countDown();
                }
            });
        });

        // then
        latch.await();
        log.info(String.valueOf(memberRepository.findAllByEmail("duplicateEmail@email.com").size()));

    }

}