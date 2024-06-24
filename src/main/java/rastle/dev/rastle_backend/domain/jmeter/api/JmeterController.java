package rastle.dev.rastle_backend.domain.jmeter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rastle.dev.rastle_backend.domain.jmeter.service.JmeterService;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@RequestMapping("/jmeter")
@RestController
@RequiredArgsConstructor
public class JmeterController {

    private final JmeterService jmeterService;

    @GetMapping("/createTest")
    public ResponseEntity<ServerResponse<?>> createTestOrder() {
        return ResponseEntity.ok(jmeterService.createTestOrder());
    }

}
