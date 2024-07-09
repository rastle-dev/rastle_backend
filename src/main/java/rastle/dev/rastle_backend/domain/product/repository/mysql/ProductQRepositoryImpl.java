package rastle.dev.rastle_backend.domain.product.repository.mysql;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimplePath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.QueryHint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.product.dto.GetProductRequest;
import rastle.dev.rastle_backend.domain.product.dto.QSimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductInfo;
import rastle.dev.rastle_backend.domain.product.dto.SimpleProductQueryResult;
import rastle.dev.rastle_backend.global.util.CustomPageRequest;

import java.util.List;

import static rastle.dev.rastle_backend.domain.product.model.QProductBase.productBase;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.FALSE;
import static rastle.dev.rastle_backend.global.common.enums.VisibleStatus.TRUE;

@RequiredArgsConstructor
@Repository
public class ProductQRepositoryImpl implements ProductQRepository {

    private final JPAQueryFactory jpaQueryFactory;

    // @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
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
            .offset(CustomPageRequest.toPageable(getProductRequest.getCustomPageRequest()).getOffset())
            .limit(CustomPageRequest.toPageable(getProductRequest.getCustomPageRequest()).getPageSize());

        orderQuery(query, CustomPageRequest.toPageable(getProductRequest.getCustomPageRequest()));
        List<SimpleProductInfo> fetched = query.fetch();
        return new SimpleProductQueryResult(fetched, getSize(getProductRequest));
    }

    private void orderQuery(JPAQuery<SimpleProductInfo> query, Pageable pageable) {
        Sort pageableSort = pageable.getSort();
        if (pageableSort.isEmpty()) {

            query.orderBy(productBase.displayOrder.asc());
        } else {
            pageableSort.stream().forEach(sort -> {
                Order order = sort.isAscending() ? Order.ASC : Order.DESC;
                String property = sort.getProperty();

                SimplePath<Object> path = Expressions.path(Object.class, productBase, property);
                OrderSpecifier<?> orderSpecifier = new OrderSpecifier(order, path);
                query.orderBy(orderSpecifier);
            });
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


