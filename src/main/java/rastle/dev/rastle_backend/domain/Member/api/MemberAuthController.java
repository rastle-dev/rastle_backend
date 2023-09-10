package rastle.dev.rastle_backend.domain.Member.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.application.EmailCertificationService;
import rastle.dev.rastle_backend.domain.Member.application.MemberAuthService;
// import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.AdminSignUpDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.EmailCertificationCheckDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.EmailCertificationDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.LoginDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.PasswordResetRequestDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberAuthDTO.SignUpDto;
import rastle.dev.rastle_backend.global.response.ServerResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        @Operation(summary = "회원가입", description = "사용자 회원가입 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원가입 성공"),
                        @ApiResponse(responseCode = "400", description = "회원가입 실패") })
        @PostMapping(value = "/signup")
        public ResponseEntity<ServerResponse<SignUpDto>> signUp(@Valid @RequestBody SignUpDto signUpDto) {
                memberAuthService.signUp(signUpDto);
                ServerResponse<SignUpDto> response = new ServerResponse<>(signUpDto);
                return ResponseEntity.ok(response);
        }

        // @Operation(summary = "관리자 회원가입", description = "관리자 회원가입 API입니다.")
        // @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "관리자
        // 회원가입 성공"),
        // @ApiResponse(responseCode = "400", description = "관리자 회원가입 실패") })
        // @PostMapping(value = "/adminSignup")
        // public ResponseEntity<ServerResponse<AdminSignUpDto>> adminSignUp(
        // @Valid @RequestBody AdminSignUpDto adminSignUpDto) {
        // memberAuthService.adminSignUp(adminSignUpDto);
        // ServerResponse<AdminSignUpDto> response = new
        // ServerResponse<>(adminSignUpDto);
        // return ResponseEntity.ok(response);
        // }

        @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 중복 확인 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 중복 확인 실패") })
        @GetMapping(value = "/checkEmail/{email}")
        public ResponseEntity<ServerResponse<?>> checkEmailExists(@PathVariable String email) {
                boolean isDuplicated = memberAuthService.isEmailDuplicated(email);
                String message = isDuplicated ? "이메일이 이미 사용 중입니다." : "이메일을 사용할 수 있습니다.";
                ServerResponse<String> response = new ServerResponse<>(message);
                HttpStatus status = isDuplicated ? HttpStatus.BAD_REQUEST : HttpStatus.OK;
                return ResponseEntity.status(status).body(response);
        }

        @Operation(summary = "로그인", description = "로그인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "로그인 성공"),
                        @ApiResponse(responseCode = "400", description = "로그인 실패") })
        @PostMapping(value = "/login")
        public ResponseEntity<ServerResponse<String>> login(@RequestBody LoginDto loginDto,
                        HttpServletResponse response) {
                ResponseEntity<String> loginResponse = memberAuthService.login(loginDto, response);
                HttpHeaders headers = loginResponse.getHeaders(); // 기존 헤더 가져오기
                ServerResponse<String> serverResponse = new ServerResponse<>(loginResponse.getBody());
                return ResponseEntity.status(loginResponse.getStatusCode()).headers(headers).body(serverResponse);
        }

        @Operation(summary = "로그아웃", description = "로그아웃 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
                        @ApiResponse(responseCode = "400", description = "로그아웃 실패") })
        @PostMapping("/logout")
        public ResponseEntity<ServerResponse<String>> logout(HttpServletRequest request, HttpServletResponse response) {
                ResponseEntity<String> logoutResponse = memberAuthService.logout(request, response);
                HttpHeaders headers = logoutResponse.getHeaders(); // 기존 헤더 가져오기
                ServerResponse<String> serverResponse = new ServerResponse<>(logoutResponse.getBody());
                return ResponseEntity.status(logoutResponse.getStatusCode()).headers(headers).body(serverResponse);
        }

        @Operation(summary = "이메일 인증 메일 전송", description = "이메일 인증 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 인증 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 인증 실패") })
        @PostMapping(value = "/emailCertification")
        public ResponseEntity<ServerResponse<String>> emailCertification(
                        @RequestBody EmailCertificationDto emailCertificationDto) throws Exception {
                String emailResponse = emailCertificationService.sendConfirmMessage(emailCertificationDto.getEmail());
                ServerResponse<String> serverResponse = new ServerResponse<>(emailResponse);
                return ResponseEntity.ok(serverResponse);
        }

        @Operation(summary = "이메일 인증 번호 확인", description = "이메일 인증 번호 확인 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "이메일 인증 번호 확인 성공"),
                        @ApiResponse(responseCode = "400", description = "이메일 인증 번호 확인 실패") })
        @PostMapping(value = "/emailCertificationCheck")
        public ResponseEntity<ServerResponse<Boolean>> emailCertificationCheck(
                        @RequestBody EmailCertificationCheckDto emailCertificationCheckDto) throws Exception {
                Boolean checkResponse = emailCertificationService.checkEmailCertification(
                                emailCertificationCheckDto.getEmail(), emailCertificationCheckDto.getCode());
                ServerResponse<Boolean> serverResponse = new ServerResponse<>(checkResponse);
                return ResponseEntity.ok(serverResponse);
        }

        @Operation(summary = "비밀번호 초기화 메일 전송", description = "비밀번호 초기화 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "비밀번호 초기화 성공"),
                        @ApiResponse(responseCode = "400", description = "비밀번호 초기화 실패") })
        @PostMapping(value = "/resetPassword")
        public ResponseEntity<ServerResponse<String>> resetPassword(
                        @RequestBody PasswordResetRequestDto passwordResetRequestDto) throws Exception {
                String resetResponse = emailCertificationService
                                .sendPasswordResetMessage(passwordResetRequestDto.getEmail());
                ServerResponse<String> serverResponse = new ServerResponse<>(resetResponse);
                return ResponseEntity.ok(serverResponse);
        }

        @Operation(summary = "액세스 토큰 재발급", description = "액세스 토큰 재발급 API입니다.")
        @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
                        @ApiResponse(responseCode = "400", description = "토큰 재발급 실패") })
        @PostMapping(value = "/refreshAccessToken")
        public ResponseEntity<ServerResponse<String>> refreshAccessToken(HttpServletRequest request) {
                if (request == null) {
                        throw new IllegalArgumentException("HttpServletRequest cannot be null");
                }
                ResponseEntity<String> result = memberAuthService.refreshAccessToken(request);
                ServerResponse<String> serverResponse = new ServerResponse<>(result.getBody());
                return new ResponseEntity<>(serverResponse, result.getHeaders(), result.getStatusCode());
        }

}
