package com.server.ordering.domain.dto.response;

import com.server.ordering.domain.Order;
import com.server.ordering.domain.OrderStatus;
import com.server.ordering.domain.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

import static lombok.AccessLevel.PROTECTED;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class OrderPreviewDto {

    private Long orderId;
    private Long reviewId;
    private Integer myOrderNumber;
    private String orderSummary;
    private String receivedTime;
    private String checkTime;
    private String cancelOrCompletedTime;
    private int totalPrice;
    private OrderType orderType;
    private Integer tableNumber;
    private OrderStatus orderStatus;

    public OrderPreviewDto(Order order) {
        this.orderId = order.getId();
        this.reviewId = order.getReview() == null ? null : order.getReview().getId();
        this.myOrderNumber = order.getMyOrderNumber();
        this.orderSummary = order.getOrderSummary();
        this.receivedTime = order.getReceivedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"));
        this.checkTime = order.getCheckedTime() == null ? null
                : order.getCheckedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"));
        this.cancelOrCompletedTime = order.getCanceledOrCompletedTime() == null ? null
                : order.getCanceledOrCompletedTime().format(DateTimeFormatter.ofPattern("yyyy/MM/dd - HH:mm:ss"));
        this.totalPrice = order.getTotalPrice();
        this.orderType = order.getOrderType();
        this.tableNumber = order.getTableNumber();
        this.orderStatus = order.getStatus();
    }
}