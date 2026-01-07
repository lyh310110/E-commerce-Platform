package edu.fdzc.mallorder.service;

import edu.fdzc.mallorder.dto.OrderDTO;
import edu.fdzc.mallorder.entity.Order;
import edu.fdzc.mallorder.entity.OrderItem;

import java.util.List;

public interface OrderService {

    /**
     * 创建订单
     */
    Order createOrder(Order order, List<OrderItem> orderItems);

    /**
     * 获取用户订单列表
     */
    List<Order> getUserOrders(Long userId, Integer status);

    /**
     * 获取订单详情
     */
    Order getOrderDetail(Long orderId);

    /**
     * 获取订单商品列表
     */
    List<OrderItem> getOrderItems(Long orderId);

    /**
     * 取消订单（只有待付款的订单可以取消）
     */
    boolean cancelOrder(Long orderId, Long userId);

    /**
     * 支付订单
     */
    boolean payOrder(Long orderId, Integer paymentType);

    /**
     * 发货
     */
    boolean shipOrder(Long orderId, Long shopId);

    /**
     * 确认收货
     */
    boolean confirmOrder(Long orderId, Long userId);
    
    /**
     * 卖家确认收款
     */
    boolean receivePayment(Long orderId, Long shopId);
    
    /**
     * 获取商家订单列表
     */
    List<OrderDTO> getShopOrders(Long shopId);
}