package rastle.dev.rastle_backend.domain.event.repository.mysql;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.MemberEventApplyHistoryDTO;
import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.ProductEventApplyHistoryDTO;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;
import rastle.dev.rastle_backend.domain.member.model.Member;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.util.Optional;

public interface EventProductApplyRepository extends JpaRepository<EventProductApply, Long> {
    boolean existsByMemberIdAndEventApplyProduct(Long memberId, ProductBase eventProduct);

    // 회원 이벤트 응모 내역 조회
    @Query("SELECT new rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO$MemberEventApplyHistoryDTO("
        +
        "pb.id, pb.name, pb.mainThumbnailImage, epa.applyDate, epa.instagramId, epa.phoneNumber) " +
        "FROM EventProductApply epa " +
        "JOIN epa.eventApplyProduct pb " +
        "WHERE epa.member.id = :memberId")
    Page<MemberEventApplyHistoryDTO> getMemberEventApplyHistoryDTOs(@Param("memberId") Long memberId,
                                                                    Pageable pageable);

    // 제품 이벤트 응모 내역 조회
    @Query("SELECT new rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO$ProductEventApplyHistoryDTO("
        +
        "m.userName, epa.applyDate, epa.instagramId, epa.phoneNumber) " +
        "FROM EventProductApply epa " +
        "JOIN epa.member m " +
        "WHERE epa.eventApplyProduct.id = :productId")
    Page<ProductEventApplyHistoryDTO> getProductEventApplyHistoryDTOs(@Param("productId") Long productId,
                                                                      Pageable pageable);

    Optional<EventProductApply> findByMemberAndEventApplyProduct(Member member, ProductBase productBase);

}
