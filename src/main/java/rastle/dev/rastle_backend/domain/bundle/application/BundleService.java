package rastle.dev.rastle_backend.domain.bundle.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rastle.dev.rastle_backend.domain.bundle.dto.BundleInfo;
import rastle.dev.rastle_backend.domain.bundle.repository.mysql.BundleRepository;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.repository.mysql.BundleProductRepository;

import java.util.List;

import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.ALL;
import static rastle.dev.rastle_backend.global.common.constants.CommonConstants.TRUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BundleService {
    private final BundleRepository bundleRepository;
    private final BundleProductRepository bundleProductRepository;

    @Transactional(readOnly = true)
    public Page<BundleInfo> getBundles(String visible, Pageable pageable) {
        if (visible.equals(ALL)) {
            return bundleRepository.getBundles(pageable);

        } else if (visible.equals(TRUE)) {
            return bundleRepository.getBundlesByVisibility(true, pageable);
        } else {
            return bundleRepository.getBundlesByVisibility(false, pageable);
        }
    }

    @Transactional(readOnly = true)
    public List<SimpleProductInfo> getBundleProducts(Long id) {
        return bundleProductRepository.getBundleProductInfosByBundleId(id);
    }
}
