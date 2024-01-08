package rastle.dev.rastle_backend.domain.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.member.application.MemberService;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.member.dto.MemberDTO.PasswordDto;
import rastle.dev.rastle_backend.domain.member.model.Address;
import rastle.dev.rastle_backend.global.response.FailApiResponses;
import rastle.dev.rastle_backend.global.response.ServerResponse;
import rastle.dev.rastle_backend.global.util.SecurityUtil;
import jakarta.servlet.http.HttpServletResponse;

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
    public ResponseEntity<ServerResponse<String>> changePassword(@RequestBody PasswordDto passwordDto) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.changePassword(currentMemberId, passwordDto.getNewPassword());
        return ResponseEntity.ok(new ServerResponse<>("비밀번호 변경 성공"));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴 API입니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "회원 탈퇴 실패") })
    @DeleteMapping("")
    public ResponseEntity<ServerResponse<String>> deleteMember(HttpServletResponse response) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteMember(response, currentMemberId);
        return ResponseEntity.ok(new ServerResponse<>("회원 탈퇴 성공"));
    }

    @Operation(summary = "회원 주소록 갱신", description = "회원 주소록 갱신 API입니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 주소록 갱신 성공"),
            @ApiResponse(responseCode = "400", description = "회원 주소록 갱신 실패") })
    @PutMapping("/updateMemberAddress")
    public ResponseEntity<ServerResponse<String>> updateMemberAddress(HttpServletResponse response,
            @RequestBody Address address) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.updateMemberAddress(currentMemberId, address);
        return ResponseEntity.ok(new ServerResponse<>("회원 주소록 갱신 성공"));
    }

    @Operation(summary = "회원 주소록 조회", description = "회원 주소록 조회 API입니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "회원 주소록 조회 성공"),
            @ApiResponse(responseCode = "400", description = "회원 주소록 조회 실패") })
    @GetMapping("/getMemberAddress")
    public ResponseEntity<ServerResponse<Address>> getMemberAddress(HttpServletResponse response) {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        Address address = memberService.getMemberAddress(currentMemberId);
        ServerResponse<Address> serverResponse = new ServerResponse<>(address);
        return ResponseEntity.ok(serverResponse);
    }

}
