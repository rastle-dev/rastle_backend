package rastle.dev.rastle_backend.domain.Bundle.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.Bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.Bundle.repository.BundleRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BundleService {
    private final BundleRepository bundleRepository;

    @Transactional(readOnly = true)
    public Page<BundleInfo> getBundles(String visible, Pageable pageable) {
        LocalDateTime current = LocalDateTime.now();
        log.info(current.toString());
        return bundleRepository.getBundles(current, pageable);
    }
}
