package rastle.dev.rastle_backend.global.oauth2.application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import rastle.dev.rastle_backend.domain.cart.model.Cart;
import rastle.dev.rastle_backend.domain.cart.repository.mysql.CartRepository;
import rastle.dev.rastle_backend.domain.coupon.model.Coupon;
import rastle.dev.rastle_backend.domain.member.model.*;
import rastle.dev.rastle_backend.domain.coupon.repository.mysql.CouponRepository;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfo;
import rastle.dev.rastle_backend.global.oauth2.OAuth2UserInfoFactory;

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

                Member member = memberRepository.findByEmail(userInfo.getEmail())
                                .orElseGet(() -> createUser(userInfo, loginType));

                return UserPrincipal.create(member, oAuth2User.getAttributes());
        }

        private Member createUser(OAuth2UserInfo memberInfo, UserLoginType loginType) {
                Member member = Member.builder()
                                .email(memberInfo.getEmail())
                                .userName(memberInfo.getName())
//                                .phoneNumber(memberInfo.getPhoneNumber())
                                .userLoginType(loginType)
                                .authority(Authority.ROLE_USER)
                                .build();
                Cart build = Cart.builder().member(member).build();
                cartRepository.save(build);
                Coupon coupon = Coupon.builder().discount(3000).name("회원가입 축하 쿠폰").member(member).build();
                couponRepository.save(coupon);
                return memberRepository.save(member);
        }
}
