package rastle.dev.rastle_backend.domain.Admin.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자 기능 API", description = "관리자 기능 관련 API입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

}
