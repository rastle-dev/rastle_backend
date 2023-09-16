package rastle.dev.rastle_backend.domain.Member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.Member.application.MemberService;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.LoginMemberInfoDto;
import rastle.dev.rastle_backend.domain.Member.dto.MemberDTO.PasswordDto;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;
import rastle.dev.rastle_backend.global.response.ServerResponse;
import rastle.dev.rastle_backend.global.util.SecurityUtil;

@Tag(name = "사용자 정보", description = "사용자 정보 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "로그인한 사용자 정보 조회", description = "로그인한 사용자 정보 조회 API입니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "사용자 정보 조회 성공"),
            @ApiResponse(responseCode = "400", description = "사용자 정보 조회 실패") })
    @GetMapping(value = "")
    public ResponseEntity<ServerResponse<LoginMemberInfoDto>> getLoginMemberInfo() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        try {
            LoginMemberInfoDto memberInfo = memberService.getLoginMemberInfo(currentMemberId);
            ServerResponse<LoginMemberInfoDto> serverResponse = new ServerResponse<>(memberInfo);
            return new ResponseEntity<>(serverResponse, HttpStatus.OK);
        } catch (NotFoundByIdException e) {
            ServerResponse<LoginMemberInfoDto> serverResponse = new ServerResponse<>(null);
            return new ResponseEntity<>(serverResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경 API입니다.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경 실패") })
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
    public ResponseEntity<ServerResponse<String>> deleteMember() {
        Long currentMemberId = SecurityUtil.getCurrentMemberId();
        memberService.deleteMember(currentMemberId);
        return ResponseEntity.ok(new ServerResponse<>("회원 탈퇴 성공"));
    }

}
