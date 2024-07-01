package rastle.dev.rastle_backend.domain.product.repository.mysql;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.QSimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductQueryResult;

import java.util.List;

import static rastle.dev.rastle_backend.domain.product.model.QProductBase.productBase;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.FALSE;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.TRUE;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ProductQRepositoryImpl implements ProductQRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public SimpleProductQueryResult getProductInfos(GetProductRequest getProductRequest) {

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
            .limit(getProductRequest.getPageable().getPageSize());
//            .orderBy(orderBy(getProductRequest));

        orderQuery(query, getProductRequest.getPageable());
        List<SimpleProductInfo> fetched = query.fetch();
        return new SimpleProductQueryResult(fetched, getSize(getProductRequest));
//        return new PageImpl<>(fetched, getProductRequest.getPageable(), getSize(getProductRequest));
    }

    private void orderQuery(JPAQuery<SimpleProductInfo> query, Pageable pageable) {
        pageable.getSort().stream().forEach(sort -> {
            Order order = sort.isAscending() ? Order.ASC : Order.DESC;
            String property = sort.getProperty();

            SimplePath<Object> path = Expressions.path(Object.class, productBase, property);
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier(order, path);
            query.orderBy(orderSpecifier);
        });
    }

//    private OrderSpecifier<?> orderBy(GetProductRequest getProductRequest) {
//        Sort sort = getProductRequest.getPageable().getSort();
//
//        if (getProductRequest.getSort().equals("displayOrder")) {
//            return productBase.displayOrder.desc();
//        } else {
//            return productBase.soldCount.desc();
//        }
//    }

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


