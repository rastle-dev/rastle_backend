package rastle.dev.rastle_backend.domain.Member.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.application.EmailCertificationService;
import rastle.dev.rastle_backend.domain.Member.application.MemberAuthService;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.EmailCertificationCheckDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.EmailCertificationDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.LoginDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.PasswordResetRequestDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.SignUpDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "사용자 인증 API", description = "사용자 인증 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberAuthController {
        private final MemberAuthService memberAuthService;
        private final EmailCertificationService emailCertificationService;

        @Operation(summary = "회원가입", description = "멤버 회원가입 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                        @ApiResponse(responseCode = "400", description = "회원가입 실패") })
        @PostMapping(value = "/signup")
        public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) {
                return ResponseEntity.ok(memberAuthService.signUp(signUpDto));
        }

        @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 중복 확인 실패") })
        @GetMapping(value = "/emailCheck/{email}")
        public ResponseEntity<Boolean> checkEmailExists(@PathVariable String email) {
                return ResponseEntity.ok(memberAuthService.isEmailExists(email));
        }

        @Operation(summary = "로그인", description = "로그인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "로그인 성공"),
                        @ApiResponse(responseCode = "400", description = "로그인 실패") })
        @PostMapping(value = "/login")
        public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
                return ResponseEntity.ok(memberAuthService.login(loginDto));
        }

        @Operation(summary = "이메일 인증", description = "이메일 인증 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 인증 실패") })
        @PostMapping(value = "/emailCertification")
        public ResponseEntity<?> emailCertification(@RequestBody EmailCertificationDto emailCertificationDto)
                        throws Exception {
                return ResponseEntity
                                .ok(emailCertificationService.sendConfirmMessage(emailCertificationDto.getEmail()));
        }

        @Operation(summary = "이메일 인증 번호 확인", description = "이메일 인증 번호 확인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 인증 번호 확인 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 인증 번호 확인 실패") })
        @PostMapping(value = "/emailCertificationCheck")
        public ResponseEntity<?> emailCertificationCheck(
                        @RequestBody EmailCertificationCheckDto emailCertificationCheckDto)
                        throws Exception {
                return ResponseEntity
                                .ok(emailCertificationService.checkEmailCertification(
                                                emailCertificationCheckDto.getEmail(),
                                                emailCertificationCheckDto.getCode()));
        }

        @Operation(summary = "비밀번호 초기화", description = "비밀번호 초기화 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "비밀번호 초기화 성공"),
                        @ApiResponse(responseCode = "400", description = "비밀번호 초기화 실패") })
        @PostMapping(value = "/resetPassword")
        public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequestDto passwordResetRequestDto)
                        throws Exception {
                return ResponseEntity
                                .ok(emailCertificationService
                                                .sendPasswordResetMessage(passwordResetRequestDto.getEmail()));
        }

}
