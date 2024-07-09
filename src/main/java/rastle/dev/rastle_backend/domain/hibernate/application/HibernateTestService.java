package rastle.dev.rastle_backend.domain.hibernate.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import rastle.dev.rastle_backend.domain.hibernate.dto.MemberInfo;
import rastle.dev.rastle_backend.domain.hibernate.dto.UpdateMember;
import rastle.dev.rastle_backend.domain.hibernate.model.TestMember;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestCartProductRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestCategoryRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestDeliveryRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestMemberRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestOrderProductRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestOrderRepository;
import rastle.dev.rastle_backend.domain.hibernate.repository.mysql.TestProductRepository;

@Service
@RequiredArgsConstructor
public class HibernateTestService {
    private final TestCartProductRepository cartProductRepository;
    private final TestCategoryRepository categoryRepository;
    private final TestDeliveryRepository deliveryRepository;
    private final TestMemberRepository memberRepository;
    private final TestOrderRepository orderRepository;
    private final TestOrderProductRepository orderProductRepository;
    private final TestProductRepository productRepository;

    @Transactional
    public void addMember() {
        TestMember newMember = TestMember.builder()
            .name("test")
            .email("user@email.com")
            .build();
            memberRepository.save(newMember);
    }

    @Transactional(readOnly = true)
    public List<MemberInfo> getMembers() {
        return memberRepository.findAll().stream().map(m -> extracted(m)).toList();
    }

    private MemberInfo extracted(TestMember m) {
        return new MemberInfo(m.getId(), m.getName(), m.getEmail());
    }

    @Transactional(readOnly = true)
    public MemberInfo getMember(Long id) {
        TestMember member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("test member not exist by id"));
        return extracted(member);
    }
    
    @Transactional
    public void updateMember(UpdateMember updateMember, Long id) {
        TestMember member = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("test member not exist by id"));
        member.update(updateMember.getName(), updateMember.getEmail());
    }
    
    @Transactional(readOnly = true)
    public List<MemberInfo> getMembersDto() {
        return memberRepository.getAllMemberInfos();
    }
}
