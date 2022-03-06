package com.omf.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.omf.constant.OrderMessage;
import com.omf.exception.ErrorConstant;
import com.omf.exception.OrderMyFoodException;
import com.omf.model.Order;
import com.omf.repository.OrderRepository;
import com.omf.rest.model.OrderCancelRequest;
import com.omf.rest.model.OrderCancelResponse;
import com.omf.rest.model.OrderRequest;
import com.omf.rest.model.OrderResponse;
import com.omf.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {
    private static final String PAID = "PAID";
    private static final String UNPAID = "UNPAID";
    private static final String NEW = "NEW";
    private static final String ORG = "_ORG";
    private static final String CANCELED = "CANCELED";
    private static final String REVISED = "_REVISED";
    private static final String ITEM_DETAIL_NAME = "itemDetail.name";
    private static final String ITEM_DETAIL = "itemDetail";
    private static final String TOTAL_PRICE = "totalPrice";
    private static final String PAYMENT_ID = "paymentId";
    private static final String STATUS = "status";
    private static final String UPDATED = "UPDATED";
    private static final String CUSTOMER_NAME = "customerName";
    private static final String ORDER_ID = "orderId";
    private OrderRepository orderRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, MongoTemplate mongoTemplate) {
        this.orderRepository = orderRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {

        List<Order> orders = orderRepository.findByCustomerNameAndItemDetailName(orderRequest.getCustomerName(),
                orderRequest.getItemDetail().getName());
        OrderResponse orderResponse = null;
        if (orders != null && orders.size() == 0) {
            orderRequest.setTransactionId(UUID.randomUUID().toString());
            orderRequest.setTotalPrice(orderRequest.getItemDetail().getPrice() *
                    orderRequest.getItemDetail().getQuantity());
            orderRequest.setOrderTime(System.currentTimeMillis());
            orderRequest.setDeliveryTime(System.currentTimeMillis() + 300000);
            orderRequest.setPaymentId(orderRequest.getCustomerCellNo() + ORG);

            orderResponse = new OrderResponse();
            Order order = new Order();
            BeanUtils.copyProperties(orderRequest, order);
            order.setStatus(NEW);
            orderRepository.save(order);

            orderResponse.setRestaurantId(order.getRestaurantId());
            orderResponse.setOrderId(order.getOrderId());
            orderResponse.setTransactionId(UUID.randomUUID().toString());
            orderResponse.setStatus(OrderMessage.CREATED);
        } else throw new OrderMyFoodException(ErrorConstant.ORDER90);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOrder(OrderRequest orderRequest) {

        OrderResponse orderResponse = null;
        List<Order> ordersForRestaurantId = orderRepository.findByRestaurantId(orderRequest.getRestaurantId());

        if (ordersForRestaurantId != null && ordersForRestaurantId.size() == 0)
            throw new OrderMyFoodException(ErrorConstant.ORDER91);

        List<Order> ordersForCustomerNameAndItem = orderRepository.findByCustomerNameAndItemDetailName(
                orderRequest.getCustomerName(), orderRequest.getItemDetail().getName());

        if (ordersForCustomerNameAndItem != null && ordersForCustomerNameAndItem.size() == 0)
            throw new OrderMyFoodException(ErrorConstant.ITEM91);

        if (ordersForCustomerNameAndItem != null && ordersForCustomerNameAndItem.size() == 1) {
            orderRequest.setTransactionId(UUID.randomUUID().toString());
            orderRequest.setTotalPrice(orderRequest.getItemDetail().getPrice() *
                    orderRequest.getItemDetail().getQuantity());
            orderRequest.setPaymentId(orderRequest.getCustomerCellNo() + REVISED);

            Query query = new Query();
            query.addCriteria(Criteria.where(CUSTOMER_NAME).is(orderRequest.getCustomerName()));
            query.addCriteria(Criteria.where(ITEM_DETAIL_NAME).is(orderRequest.getItemDetail().getName()));

            Update update = new Update();
            update.set(ITEM_DETAIL, orderRequest.getItemDetail());
            update.set(TOTAL_PRICE, orderRequest.getTotalPrice());
            update.set(PAYMENT_ID, orderRequest.getCustomerCellNo() + REVISED);
            update.set(STATUS, UPDATED);
            UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Order.class);

            if (updateResult.getModifiedCount() == 1) {
                orderResponse = new OrderResponse();
                orderResponse.setTransactionId(UUID.randomUUID().toString());
                orderResponse.setRestaurantId(orderRequest.getRestaurantId());
                orderResponse.setStatus(OrderMessage.MODIFIED);
                orderResponse.setOrderId(ordersForCustomerNameAndItem.get(0).getOrderId());
            } else throw new OrderMyFoodException(ErrorConstant.ORDER105);
        } else throw new OrderMyFoodException(ErrorConstant.ORDER90);
        return orderResponse;
    }

    @Override
    public OrderResponse updateOderAfterPayment(Order order) {
        OrderResponse orderResponse = null;
        Query query = new Query();
        query.addCriteria(Criteria.where(ORDER_ID).is(order.getOrderId()));

        Update update = new Update();
        update.set(STATUS, PAID);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Order.class);
        if (updateResult.getModifiedCount() == 1) {
            orderResponse = new OrderResponse();
            orderResponse.setTransactionId(UUID.randomUUID().toString());
            orderResponse.setStatus(OrderMessage.PAID);
            orderResponse.setPaymentStatus(PAID);
        } else {
            orderResponse = new OrderResponse();
            orderResponse.setTransactionId(UUID.randomUUID().toString());
            orderResponse.setStatus(OrderMessage.NOT_PAID);
            orderResponse.setPaymentStatus(UNPAID);
        }
        return orderResponse;
    }

    @Override
    public OrderCancelResponse cancelOrder(OrderCancelRequest orderCancelRequest) {
        Arrays.stream(orderCancelRequest.getOrderIds()).forEach(
                orderId -> {
                    Order order = orderRepository.getByOrderIdAndCustomerName(orderId, orderCancelRequest.getCustomerName());
                    if (order == null) throw new OrderMyFoodException(ErrorConstant.ORDER92);
                }
        );
        Query query = new Query();
        query.addCriteria(Criteria.where(ORDER_ID).in(orderCancelRequest.getOrderIds()));
        query.addCriteria(Criteria.where(CUSTOMER_NAME).is(orderCancelRequest.getCustomerName()));
        Update update = new Update();
        update.set(STATUS, CANCELED);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Order.class);
        OrderCancelResponse orderCancelResponse;
        if (updateResult.getModifiedCount() != 0 && updateResult.getModifiedCount() == orderCancelRequest.getOrderIds().length) {
            orderCancelResponse = new OrderCancelResponse();
            orderCancelResponse.setTransactionId(UUID.randomUUID().toString());
            orderCancelResponse.setStatus(OrderMessage.CANCELED);
        } else if (updateResult.getModifiedCount() != 0 && updateResult.getModifiedCount() <
                orderCancelRequest.getOrderIds().length) throw new OrderMyFoodException(ErrorConstant.ORDER107);
        else
            throw new OrderMyFoodException(ErrorConstant.ORDER106);
        return orderCancelResponse;
    }

    @Override
    public List<Order> getOrderByCustomerName(String customerName) {
        List<Order> orders = orderRepository.findByCustomerName(customerName);
        if (orders != null && orders.size() == 0) throw new OrderMyFoodException(ErrorConstant.CUSTOMER91);
        return orders;
    }

    @Override
    public List<Order> getByOrderId(String orderId) {
        List<Order> orders = orderRepository.findByOrderId(orderId);
        if (orders != null && orders.size() == 0) throw new OrderMyFoodException(ErrorConstant.ORDER93);
        return orders;
    }
}
