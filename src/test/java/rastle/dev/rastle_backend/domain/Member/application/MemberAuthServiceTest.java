package rastle.dev.rastle_backend.domain.Member.application;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.LoginDto;

@ExtendWith(MockitoExtension.class)
public class MemberAuthServiceTest {
    @InjectMocks
    private MemberAuthService memberAuthService;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testLogin() {
        String email = "yslim162@naver.com";
        String password = "1234";
        LoginDto loginDto = LoginDto.builder().email(email).password(password).build();

        Authentication expectedAuthentication = new UsernamePasswordAuthenticationToken(email, password);

        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(expectedAuthentication);
        String accessToken = memberAuthService.login(loginDto);

        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(email, password));
    }

    @Test
    void testSignUp() {

    }
}
