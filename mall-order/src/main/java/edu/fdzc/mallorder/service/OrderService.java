package edu.fdzc.mallorder.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.mallcommon.constant.OrderConstant;
import edu.fdzc.mallorder.entity.Order;
import edu.fdzc.mallorder.entity.OrderItem;
import edu.fdzc.mallorder.mapper.OrderItemMapper;
import edu.fdzc.mallorder.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    /**
     * 创建订单
     */
    @Transactional
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        // 生成订单号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);

        // 设置订单状态
        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_PAYMENT);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 保存订单
        int result = orderMapper.insert(order);
        if (result <= 0) {
            throw new RuntimeException("订单创建失败");
        }

        // 保存订单商品
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setOrderNo(orderNo);
            item.setCreateTime(LocalDateTime.now());
            orderItemMapper.insert(item);
        }

        return order;
    }

    /**
     * 获取用户订单列表
     */
    public List<Order> getUserOrders(Long userId, Integer status) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("create_time");

        if (status != null) {
            queryWrapper.eq("status", status);
        }

        return orderMapper.selectList(queryWrapper);
    }

    /**
     * 获取订单详情
     */
    public Order getOrderDetail(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    /**
     * 获取订单商品列表
     */
    public List<OrderItem> getOrderItems(Long orderId) {
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return orderItemMapper.selectList(queryWrapper);
    }

    /**
     * 取消订单（只有待付款的订单可以取消）
     */
    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        // 只有待付款的订单可以取消
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_PAYMENT) {
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_CANCELLED);
        order.setCloseTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return orderMapper.updateById(order) > 0;
    }

    /**
     * 支付订单
     */
    @Transactional
    public boolean payOrder(Long orderId, Integer paymentType) {
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            return false;
        }

        // 只有待付款的订单可以支付
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_PAYMENT) {
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_SHIPMENT);
        order.setPaymentType(paymentType);
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return orderMapper.updateById(order) > 0;
    }

    /**
     * 发货
     */
    @Transactional
    public boolean shipOrder(Long orderId, Long shopId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getShopId().equals(shopId)) {
            return false;
        }

        // 只有待发货的订单可以发货
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_SHIPMENT) {
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_RECEIPT);
        order.setConsignTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return orderMapper.updateById(order) > 0;
    }

    /**
     * 确认收货
     */
    @Transactional
    public boolean confirmOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            return false;
        }

        // 只有待收货的订单可以确认收货
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_RECEIPT) {
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_COMPLETED);
        order.setEndTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        return orderMapper.updateById(order) > 0;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 时间戳 + 随机数
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        return timestamp + random;
    }
}