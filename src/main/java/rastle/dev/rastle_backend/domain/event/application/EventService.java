package rastle.dev.rastle_backend.domain.event.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.events.Event;

import rastle.dev.rastle_backend.domain.event.dto.EventInfo;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.EventProductApplyInfoDTO;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventProductApplyRepository;
import rastle.dev.rastle_backend.domain.event.repository.mysql.EventRepository;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.member.repository.mysql.MemberRepository;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;
import rastle.dev.rastle_backend.domain.product.repository.mysql.EventProductRepository;
import rastle.dev.rastle_backend.global.error.exception.NotFoundByIdException;

import java.util.List;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final EventProductRepository eventProductRepository;
    private final EventProductApplyRepository eventProductApplyRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Page<EventInfo> getEventInfo(String visible, Pageable pageable) {
        if (visible.equals(ALL)) {
            return eventRepository.getEventInfo(pageable);
        } else if (visible.equals(TRUE)) {
            return eventRepository.getEventInfoByVisibility(true, pageable);
        } else {
            return eventRepository.getEventInfoByVisibility(false, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<SimpleProductInfo> getEventProducts(Long id) {
        return eventProductRepository.getEventProductInfosByEventId(id);
    }

    /**
     * 이벤트 응모 신청
     * 
     * @param memberId
     * @param eventProductApplyDTO
     */
    public void applyEventProduct(Long currentMemberId, EventProductApplyDTO eventProductApplyDTO) {
        Member member = memberRepository.findById(currentMemberId).orElseThrow(NotFoundByIdException::new);

        EventProductApply eventProductApply = EventProductApply.builder()
                .member(member)
                .phoneNumber(eventProductApplyDTO.getEventPhoneNumber())
                .instagramId(eventProductApplyDTO.getInstagramId())
                .eventApplyProduct(ProductBase.builder().id(eventProductApplyDTO.getEventProductId()).build())
                .build();

        eventProductApplyRepository.save(eventProductApply);
    }

    /**
     * 이벤트 응모 신청 내역 조회
     * 
     * @param memberId
     */
    @Transactional(readOnly = true)
    public List<EventProductApplyInfoDTO> getEventProductApplyInfoDTOs(Long memberId) {
        return eventProductApplyRepository.getEventProductApplyInfoDTOs(memberId);
    }

}
