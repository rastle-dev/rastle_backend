package rastle.dev.rastle_backend.domain.admin.repository.mysql;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import rastle.dev.rastle_backend.domain.admin.dto.GetMemberOrderCondition;
import rastle.dev.rastle_backend.domain.admin.dto.GetMemberOrderInfo;
import rastle.dev.rastle_backend.domain.admin.dto.QGetMemberOrderInfo;
import rastle.dev.rastle_backend.domain.order.model.OrderDetail;
import rastle.dev.rastle_backend.global.common.enums.OrderStatus;

import java.util.HashSet;

import static com.querydsl.core.types.Order.DESC;
import static rastle.dev.rastle_backend.domain.delivery.model.QDelivery.delivery;
import static rastle.dev.rastle_backend.domain.order.model.QOrderDetail.orderDetail;
import static rastle.dev.rastle_backend.domain.order.model.QOrderProduct.orderProduct;
import static rastle.dev.rastle_backend.domain.payment.model.QPayment.payment;

@Slf4j
@RequiredArgsConstructor
@Repository
public class MemberOrderQRepositoryImpl implements MemberOrderQRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<GetMemberOrderInfo> getMemberOrderInfo(GetMemberOrderCondition condition) {
        JPAQuery<GetMemberOrderInfo> query = jpaQueryFactory.select(
                new QGetMemberOrderInfo(
                    orderProduct.orderStatus,
                    payment.paidAt,
                    orderDetail.orderNumber,
                    orderProduct.productOrderNumber,
                    orderProduct.name,
                    orderProduct.color,
                    orderProduct.size,
                    orderProduct.count,
                    delivery.userName,
                    delivery.tel,
                    delivery.email,
                    delivery.address,
                    delivery.postcode,
                    delivery.deliveryService,
                    delivery.msg,
                    orderProduct.trackingNumber,
                    payment.impId,
                    orderProduct.price,
                    orderProduct.totalPrice,
                    orderDetail.orderPrice,
                    payment.paymentPrice,
                    delivery.deliveryPrice,
                    delivery.islandDeliveryPrice,
                    payment.paymentMethod,
                    payment.couponAmount,
                    orderProduct.cancelAmount,
                    orderProduct.cancelRequestAmount

                )
            ).from(orderProduct)
            .leftJoin(orderProduct.orderDetail, orderDetail)
            .leftJoin(orderDetail.delivery, delivery)
            .leftJoin(orderDetail.payment, payment)
            .where(
                orderStatus(condition),
                receiverName(condition)
            )
            .orderBy(orderByOrderNumber())
            .offset(condition.getPageable().getOffset())
            .limit(condition.getPageable().getPageSize());


        return new PageImpl<>(query.fetch(), condition.getPageable(), getSize(condition));
    }

    private Long getSize(GetMemberOrderCondition condition) {
        JPAQuery<Long> countQuery = jpaQueryFactory.select(
                orderProduct.count()
            ).from(orderProduct)
            .leftJoin(orderProduct.orderDetail, orderDetail)
            .leftJoin(orderDetail.delivery, delivery)
            .leftJoin(orderDetail.payment, payment)
            .where(
                orderStatus(condition),
                receiverName(condition)
            )
            .groupBy(orderProduct);
        return countQuery.fetchOne();
    }

    private OrderSpecifier orderByOrderNumber() {
        PathBuilder entityPath = new PathBuilder(OrderDetail.class, "orderDetail");
        return new OrderSpecifier(DESC, entityPath.get("orderNumber"));
    }

    private BooleanExpression orderStatus(GetMemberOrderCondition condition) {

        if (condition.getOrderStatus() == null) {
            return null;
        }
        HashSet<OrderStatus> set = new HashSet<>();
        for (String os : condition.getOrderStatus()) {
            set.add(OrderStatus.getFromStatus(os));
        }
        return orderDetail.orderStatus.in(set);
    }

    private BooleanExpression receiverName(GetMemberOrderCondition condition) {
        if (condition.getReceiverName() == null) {
            return null;
        }
        return delivery.userName.eq(condition.getReceiverName());
    }
}
