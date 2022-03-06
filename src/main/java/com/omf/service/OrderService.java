package com.omf.service;

import com.omf.model.Order;
import com.omf.rest.model.OrderCancelRequest;
import com.omf.rest.model.OrderCancelResponse;
import com.omf.rest.model.OrderRequest;
import com.omf.rest.model.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);

    public OrderResponse updateOrder(OrderRequest orderRequest);

    public OrderResponse updateOderAfterPayment(Order order);

    public OrderCancelResponse cancelOrder(OrderCancelRequest orderCancelRequest);

    public List<Order> getOrderByCustomerName(String customerName);

    public List<Order> getByOrderId(String orderId);
}
