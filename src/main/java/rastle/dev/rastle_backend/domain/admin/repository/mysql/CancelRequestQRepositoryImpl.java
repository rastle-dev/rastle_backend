package rastle.dev.rastle_backend.domain.admin.repository.mysql;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.admin.dto.GetCancelRequestCondition;
import rastle.dev.rastle_backend.domain.admin.dto.GetCancelRequestInfo;
import rastle.dev.rastle_backend.domain.admin.dto.QGetCancelRequestInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;

import static com.querydsl.core.types.Order.DESC;
import static rastle.dev.rastle_backend.domain.delivery.model.QDelivery.delivery;
import static rastle.dev.rastle_backend.domain.order.model.QCancelRequest.cancelRequest;
import static rastle.dev.rastle_backend.domain.order.model.QOrderDetail.orderDetail;
import static rastle.dev.rastle_backend.domain.order.model.QOrderProduct.orderProduct;
import static rastle.dev.rastle_backend.domain.payment.model.QPayment.payment;

@Repository
@RequiredArgsConstructor
public class CancelRequestQRepositoryImpl implements CancelRequestQRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<GetCancelRequestInfo> getCancelRequestInfo(GetCancelRequestCondition condition) {
        JPAQuery<GetCancelRequestInfo> query = jpaQueryFactory.select(
                new QGetCancelRequestInfo(
                    payment.impId,
                    cancelRequest.id,
                    orderDetail.orderNumber,
                    orderProduct.productOrderNumber,
                    orderProduct.orderStatus,
                    orderProduct.totalPrice,
                    orderProduct.name,
                    orderProduct.color,
                    orderProduct.size,
                    orderProduct.count,
                    cancelRequest.reason,
                    delivery.userName,
                    delivery.tel,
                    delivery.email,
                    delivery.address,
                    delivery.postcode
                )
            ).from(cancelRequest)
            .leftJoin(orderProduct).on(orderProduct.productOrderNumber.eq(cancelRequest.productOrderNumber))
            .leftJoin(cancelRequest.orderDetail, orderDetail)
            .leftJoin(orderDetail.payment, payment)
            .leftJoin(orderDetail.delivery, delivery)
            .where(
                orderNumber(condition),
                receiverName(condition),
                status(condition)
            )
            .orderBy(orderByOrderNumber())
            .offset(condition.getPageable().getOffset())
            .limit(condition.getPageable().getPageSize());


        return new PageImpl<>(query.fetch(), condition.getPageable(), getSize(condition));
    }

    private Long getSize(GetCancelRequestCondition condition) {
        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                cancelRequest.count()
            ).from(orderProduct)
            .from(cancelRequest)
            .leftJoin(orderProduct).on(orderProduct.productOrderNumber.eq(cancelRequest.productOrderNumber))
            .leftJoin(cancelRequest.orderDetail, orderDetail)
            .leftJoin(orderDetail.payment, payment)
            .leftJoin(orderDetail.delivery, delivery)
            .where(
                orderNumber(condition),
                receiverName(condition)
            )
            .groupBy(cancelRequest);
        return countQuery.fetchOne();
    }

    private BooleanExpression status(GetCancelRequestCondition condition) {
        if (condition.getCancelRequestStatus() == null) {
            return null;
        }
        return cancelRequest.cancelRequestStatus.eq(condition.getCancelRequestStatus());
    }

    private BooleanExpression orderNumber(GetCancelRequestCondition condition) {
        if (condition.getOrderNumber() == null) {
            return null;
        }
        return orderDetail.orderNumber.eq(condition.getOrderNumber());
    }

    private BooleanExpression receiverName(GetCancelRequestCondition condition) {
        if (condition.getReceiverName() == null) {
            return null;
        }
        return delivery.userName.eq(condition.getReceiverName());
    }

    private OrderSpecifier orderByOrderNumber() {
        PathBuilder entityPath = new PathBuilder(OrderDetail.class, "orderDetail");
        return new OrderSpecifier(DESC, entityPath.get("orderNumber"));
    }
}
