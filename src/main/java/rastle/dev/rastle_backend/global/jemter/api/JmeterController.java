package rastle.dev.rastle_backend.global.jemter.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rastle.dev.rastle_backend.global.response.ServerResponse;

@RequestMapping("/jmeter")
@RestController
@RequiredArgsConstructor
public class JmeterController {
    @GetMapping("/createTest")
    public ResponseEntity<ServerResponse<?>> createTestOrder() {

    }

}
