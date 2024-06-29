package rastle.dev.rastle_backend.domain.product.repository.mysql;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.QSimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;

import static rastle.dev.rastle_backend.domain.product.model.QProductBase.productBase;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.FALSE;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.TRUE;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductQRepositoryImpl implements ProductQRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SimpleProductInfo> getProductInfos(GetProductRequest getProductRequest) {

        JPAQuery<SimpleProductInfo> query = jpaQueryFactory.select(
                new QSimpleProductInfo(
                    productBase.id,
                    productBase.name,
                    productBase.price,
                    productBase.mainThumbnailImage,
                    productBase.subThumbnailImage,
                    productBase.discountPrice,
                    productBase.displayOrder,
                    productBase.visible,
                    productBase.category.id,
                    productBase.bundle.id,
                    productBase.event.id,
                    productBase.eventApplyCount,
                    productBase.soldOut,
                    productBase.soldCount
                )
            ).from(productBase)
            .where(
                visible(getProductRequest),
                bundle(getProductRequest),
                event(getProductRequest)
            )
            .groupBy(productBase)
            .offset(getProductRequest.getPageable().getOffset())
            .limit(getProductRequest.getPageable().getPageSize())
            .orderBy(productBase.displayOrder.desc());

        return new PageImpl<>(query.fetch(), getProductRequest.getPageable(), getSize(getProductRequest));
    }


    private Long getSize(GetProductRequest getProductRequest) {

        JPAQuery<Long> query = jpaQueryFactory.select(
               productBase.count()
            ).from(productBase)
            .where(
                visible(getProductRequest),
                bundle(getProductRequest),
                event(getProductRequest)

            ).groupBy(productBase.id);
        if (query.fetchOne() != null) {
            return query.fetchOne();
        }
        return 0L;
    }

    private BooleanExpression visible(GetProductRequest getProductRequest) {
        if (getProductRequest.getVisibleStatus().equals(FALSE) || getProductRequest.getVisibleStatus().equals(TRUE)) {
            if (getProductRequest.getVisibleStatus().equals(FALSE)) {
                return productBase.visible.eq(false);
            } else {
                return productBase.visible.eq(true);
            }
        }
        return null;
    }

    private BooleanExpression bundle(GetProductRequest getProductRequest) {
        if (getProductRequest.getBundleId() == null) {
            return null;
        }
        return productBase.bundle.id.eq(getProductRequest.getBundleId());

    }

    private BooleanExpression event(GetProductRequest getProductRequest) {
        if (getProductRequest.getEventId() == null) {
            return productBase.event.id.isNull();
        }
        return productBase.event.id.eq(getProductRequest.getEventId());
    }

}


