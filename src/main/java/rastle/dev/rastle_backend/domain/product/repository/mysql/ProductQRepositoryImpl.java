package rastle.dev.rastle_backend.domain.product.repository.mysql;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.QSimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.model.ProductBase;

import java.time.LocalDateTime;
import java.time.ZoneId;

import static rastle.dev.rastle_backend.domain.order.model.QOrderProduct.orderProduct;
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
                    productBase.soldCount
                )
            ).from(productBase)
            .leftJoin(productBase.orderProducts, orderProduct)
            .where(
                visible(getProductRequest),
                period(),
                bundle(getProductRequest),
                event(getProductRequest)
            )
            .groupBy(productBase)
            .offset(getProductRequest.getPageable().getOffset())
            .limit(getProductRequest.getPageable().getPageSize());

        orderBy(query, getProductRequest);

        return new PageImpl<>(query.fetch(), getProductRequest.getPageable(), getSize(getProductRequest));
    }

    private void orderBy(JPAQuery<SimpleProductInfo> query, GetProductRequest getProductRequest) {
        PathBuilder entityPath = new PathBuilder(ProductBase.class, "productBase");
        for (Sort.Order order : getProductRequest.getPageable().getSort()) {
            String property = order.getProperty();
            log.info(String.valueOf(order.isAscending()));

            log.info(order.getProperty());
            log.info(String.valueOf(order.getDirection()));
            query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC, entityPath.get(order.getProperty())));

        }

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
        return query.fetchOne();
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

    private BooleanExpression period() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        LocalDateTime minusDay = now.minusDays(30);

        return orderProduct.createdTime.goe(minusDay);

    }

    private BooleanExpression bundle(GetProductRequest getProductRequest) {
        if (getProductRequest.getBundleId() == null) {
            return null;
        }
        return productBase.bundle.id.eq(getProductRequest.getBundleId());

    }

    private BooleanExpression event(GetProductRequest getProductRequest) {
        if (getProductRequest.getEventId() == null) {
            return null;
        }
        return productBase.event.id.eq(getProductRequest.getEventId());
    }

}


