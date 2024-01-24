package rastle.dev.rastle_backend.domain.member.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.member.application.MemberService;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.NewPhoneNumberDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.PasswordDto;
import rastle.dev.rastle_backend.domain.member.model.Address;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

@Tag(name = "회원 정보", description = "회원 정보 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "로그인한 회원 정보 조회", description = "현재 로그인한 회원의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공", content = @Content(schema = @Schema(implementation = LoginMemberInfoDto.class)))
    @FailApiResponses
    @GetMapping(value = "")
    public ResponseEntity<ServerResponse<LoginMemberInfoDto>> getLoginMemberInfo() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();

        LoginMemberInfoDto memberInfo = memberService.getLoginMemberInfo(currentMemberId);
        ServerResponse<LoginMemberInfoDto> serverResponse = new ServerResponse<>(memberInfo);
        return new ResponseEntity<>(serverResponse, HttpStatus.OK);
    }

    @Operation(summary = "비밀번호 변경", description = "회원의 비밀번호를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공")
    @FailApiResponses
    @PutMapping("/changePassword")
    public ResponseEntity<ServerResponse<String>> changePassword(@Valid @RequestBody PasswordDto passwordDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.changePassword(currentMemberId, passwordDto.getNewPassword());
        return ResponseEntity.ok(new ServerResponse<>("비밀번호 변경 성공"));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 진행합니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패") })
    @DeleteMapping("")
    public ResponseEntity<ServerResponse<String>> deleteMember(HttpServletResponse response) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteMember(response, currentMemberId);
        return ResponseEntity.ok(new ServerResponse<>("회원 탈퇴 성공"));
    }

    @Operation(summary = "회원 주소지 갱신", description = "회원의 주소지를 갱신합니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 주소지 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "회원 주소지 갱신 실패") })
    @PutMapping("/updateMemberAddress")
    public ResponseEntity<ServerResponse<String>> updateMemberAddress(HttpServletResponse response,
            @RequestBody Address address) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.updateMemberAddress(currentMemberId, address);
        return ResponseEntity.ok(new ServerResponse<>("회원 주소지 갱신 성공"));
    }

    @Operation(summary = "회원 주소지 조회", description = "회원의 주소지를 조회합니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 주소지 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 주소지 조회 실패") })
    @GetMapping("/getMemberAddress")
    public ResponseEntity<ServerResponse<Address>> getMemberAddress(HttpServletResponse response) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Address address = memberService.getMemberAddress(currentMemberId);
        ServerResponse<Address> serverResponse = new ServerResponse<>(address);
        return ResponseEntity.ok(serverResponse);
    }

    @Operation(summary = "회원 전화번호 갱신", description = "회원의 전화번호를 갱신합니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 전화번호 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "회원 전화번호 갱신 실패") })
    @PutMapping("/updateMemberPhoneNumber")
    public ResponseEntity<ServerResponse<String>> updateMemberPhoneNumber(HttpServletResponse response,
            @Valid @RequestBody NewPhoneNumberDto newPhoneNumberDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.updateMemberPhoneNumber(currentMemberId, newPhoneNumberDto.getNewPhoneNumber());
        return ResponseEntity.ok(new ServerResponse<>("회원 전화번호 갱신 성공"));
    }

}
