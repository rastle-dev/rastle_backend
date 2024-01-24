package rastle.dev.rastle_backend.domain.event.repository.mysql;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO.EventProductApplyInfoDTO;
import rastle.dev.rastle_backend.domain.event.model.EventProductApply;

public interface EventProductApplyRepository extends JpaRepository<EventProductApply, Long> {
    @Query("SELECT new rastle.dev.rastle_backend.domain.event.dto.EventProductApplyDTO$EventProductApplyInfoDTO(" +
            "pb.name, epa.applyDate, epa.instagramId, epa.phoneNumber) " +
            "FROM EventProductApply epa " +
            "JOIN epa.eventApplyProduct pb " +
            "WHERE epa.member.id = :memberId")
    List<EventProductApplyInfoDTO> getEventProductApplyInfoDTOs(@Param("memberId") Long memberId);
}
