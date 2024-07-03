package rastle.dev.rastle_backend.global.oauth2.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartRepository;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.member.dto.MemberAuthDTO.UserPrincipalInfoDto;
import rastle.dev.rastle_backend.domain.member.model.*;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.global.error.exception.GlobalException;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfoFactory;

import static rastle.dev.rastle_backend.global.common.enums.CouponStatus.NOT_USED;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CouponRepository couponRepository;

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        UserLoginType loginType = UserLoginType
                .valueOfLabel(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(loginType.getType(),
                oAuth2User.getAttributes());

        // Member member = memberRepository.findByEmail(userInfo.getEmail())
        // .orElseGet(() -> createUser(userInfo, loginType));

        // return UserPrincipal.create(member, oAuth2User.getAttributes());

        String formattedPhoneNumber = formatPhoneNumber(userInfo.getPhoneNumber());
        userInfo.setPhoneNumber(formattedPhoneNumber);

        UserPrincipalInfoDto userPrincipalInfoDto = memberRepository
                .findUserPrincipalInfoByEmailAndNameAndPhoneNumber(userInfo.getEmail(), userInfo.getName(),
                        userInfo.getPhoneNumber())
                .orElseGet(() -> createUser(userInfo, loginType));

        return UserPrincipal.create(userPrincipalInfoDto); // 수정된 부분
    }

    private UserPrincipalInfoDto createUser(OAuth2UserInfo memberInfo, UserLoginType loginType) {
        if (memberRepository.existsByEmailAndUserLoginTypeAndDeleted(memberInfo.getEmail(), loginType, true)) {
            throw new GlobalException(
                    "이미 탈퇴한 소셜 로그인 유저의 재 로그인 요청입니다. " + memberInfo.getEmail() + " " + loginType.getType(),
                    HttpStatus.CONFLICT);
        }
        Member member = Member.builder()
                .email(memberInfo.getEmail())
                .userName(memberInfo.getName())
                // .phoneNumber(formatPhoneNumber(memberInfo.getPhoneNumber()))
                .phoneNumber(memberInfo.getPhoneNumber())
                .userLoginType(loginType)
                .authority(Authority.ROLE_USER)
                .deleted(false)
                .build();

        RecipientInfo recipientInfo = new RecipientInfo();
        recipientInfo.setRecipientName(memberInfo.getName());
        recipientInfo.setRecipientPhoneNumber(memberInfo.getPhoneNumber());
        member.updateRecipientInfo(recipientInfo);

        Cart build = Cart.builder().member(member).build();
        cartRepository.save(build);

        Coupon coupon = Coupon.builder().discount(3000).name("회원가입 축하 쿠폰").member(member)
                .couponStatus(NOT_USED).build();
        couponRepository.save(coupon);

        memberRepository.save(member);
        if (member.getEmail() == null) {
            member.updateEmail("nullemail_" + member.getId() + "@email.com");
        }

        return UserPrincipalInfoDto.builder()
                .id(member.getId())
                .password(member.getPassword())
                .userLoginType(member.getUserLoginType())
                .authority(member.getAuthority())
                .build();
    }

    private String formatPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return "";
        }

        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        if (phoneNumber.startsWith("82")) {
            phoneNumber = "0" + phoneNumber.substring(2);
        }

        return phoneNumber;
    }
}
