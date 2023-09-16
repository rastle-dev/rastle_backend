package rastle.dev.rastle_backend.domain.Event.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rastle.dev.rastle_backend.domain.Event.application.EventService;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@Tag(name = "이벤트 관련 API", description = "이벤트 관련 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {
    private final EventService eventService;

    @Operation(summary = "이벤트 조회 API", description = "이벤트 조회 API 입니다")
    @GetMapping("")
    public ResponseEntity<ServerResponse<?>> getEventDetail(Pageable pageable) {
        return ResponseEntity.ok(new ServerResponse<>(eventService.getEventInfo(pageable)));
    }
}
