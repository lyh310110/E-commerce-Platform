package edu.fdzc.mallorder.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fdzc.mallcommon.constant.OrderConstant;
import edu.fdzc.mallorder.dto.OrderDTO;
import edu.fdzc.mallorder.entity.Order;
import edu.fdzc.mallorder.entity.OrderItem;
import edu.fdzc.mallorder.mapper.OrderItemMapper;
import edu.fdzc.mallorder.mapper.OrderMapper;
import edu.fdzc.mallorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    /**
     * 创建订单
     */
    @Override
    @Transactional
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        log.info("开始创建订单: {}，订单项数量: {}", order, orderItems.size());
        
        // 生成订单号
        String orderNo = generateOrderNo();
        order.setOrderNo(orderNo);
        log.info("生成订单号: {}", orderNo);

        // 设置订单状态
        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_PAYMENT);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 保存订单
        int result = orderMapper.insert(order);
        if (result <= 0) {
            log.error("订单保存失败: {}", order);
            throw new RuntimeException("订单主表保存失败");
        }
        log.info("订单保存成功，订单ID: {}", order.getId());

        // 保存订单商品
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getId());
            item.setOrderNo(orderNo);
            item.setCreateTime(LocalDateTime.now());
            int itemResult = orderItemMapper.insert(item);
            if (itemResult <= 0) {
                log.error("订单项保存失败: {}", item);
                throw new RuntimeException("订单项保存失败");
            }
            log.info("订单项保存成功: {}", item);
        }

        log.info("订单创建完成: {}", order);
        return order;
    }

    /**
     * 获取用户订单列表
     */
    @Override
    public List<Order> getUserOrders(Long userId, Integer status) {
        log.info("获取用户订单列表，用户ID: {}, 状态: {}", userId, status);
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                .orderByDesc("create_time");

        if (status != null) {
            queryWrapper.eq("status", status);
        }

        List<Order> orders = orderMapper.selectList(queryWrapper);
        log.info("获取到用户订单数量: {}", orders.size());
        return orders;
    }

    /**
     * 获取订单详情
     */
    @Override
    public Order getOrderDetail(Long orderId) {
        log.info("获取订单详情，订单ID: {}", orderId);
        return orderMapper.selectById(orderId);
    }

    /**
     * 获取订单商品列表
     */
    @Override
    public List<OrderItem> getOrderItems(Long orderId) {
        log.info("获取订单商品列表，订单ID: {}", orderId);
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id", orderId);
        return orderItemMapper.selectList(queryWrapper);
    }

    /**
     * 取消订单（只有待发货的订单可以取消）
     */
    @Override
    @Transactional
    public boolean cancelOrder(Long orderId, Long userId) {
        log.info("=== 开始取消订单流程 ===");
        log.info("取消订单参数: 订单ID={}, 用户ID={}", orderId, userId);
        
        // 1. 检查订单是否存在
        Order order = orderMapper.selectById(orderId);
        log.info("查询到的订单: {}", order);
        
        if (order == null) {
            log.error("订单不存在，订单ID: {}", orderId);
            return false;
        }
        
        // 2. 检查用户是否匹配
        log.info("订单用户ID: {}, 请求用户ID: {}", order.getUserId(), userId);
        if (!order.getUserId().equals(userId)) {
            log.error("用户不匹配，取消订单失败");
            return false;
        }
        
        // 3. 检查订单状态是否允许取消（允许待付款和待发货状态的订单取消）
        log.info("当前订单状态: {}, 允许取消的状态: 待付款({})、待发货({})", order.getStatus(), OrderConstant.ORDER_STATUS_PENDING_PAYMENT, OrderConstant.ORDER_STATUS_PENDING_SHIPMENT);
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_PAYMENT && 
            order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_SHIPMENT) {
            log.error("订单状态不允许取消，当前状态: {}", order.getStatus());
            return false;
        }
        
        // 4. 更新订单状态
        order.setStatus(OrderConstant.ORDER_STATUS_CANCELLED);
        order.setCloseTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        log.info("更新后的订单信息: {}", order);
        
        // 5. 执行数据库更新
        int updateResult = orderMapper.updateById(order);
        log.info("数据库更新结果: 更新行数={}", updateResult);
        
        boolean success = updateResult > 0;
        log.info("取消订单{}", success ? "成功" : "失败");
        log.info("=== 取消订单流程结束 ===");
        
        return success;
    }

    /**
     * 支付订单
     */
    @Override
    @Transactional
    public boolean payOrder(Long orderId, Integer paymentType) {
        log.info("支付订单，订单ID: {}, 支付方式: {}", orderId, paymentType);
        Order order = orderMapper.selectById(orderId);
        if (order == null) {
            log.error("订单不存在，订单ID: {}", orderId);
            return false;
        }

        // 只有待付款的订单可以支付
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_PAYMENT) {
            log.error("订单状态不允许支付，当前状态: {}", order.getStatus());
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_SHIPMENT);
        order.setPaymentType(paymentType);
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        boolean success = orderMapper.updateById(order) > 0;
        log.info("支付订单{}", success ? "成功" : "失败");
        return success;
    }

    /**
     * 发货
     */
    @Override
    @Transactional
    public boolean shipOrder(Long orderId, Long shopId) {
        log.info("发货，订单ID: {}, 店铺ID: {}", orderId, shopId);
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getShopId().equals(shopId)) {
            log.error("订单不存在或店铺不匹配，订单ID: {}, 店铺ID: {}", orderId, shopId);
            return false;
        }

        // 只有待发货的订单可以发货
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_SHIPMENT) {
            log.error("订单状态不允许发货，当前状态: {}", order.getStatus());
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_PENDING_RECEIPT);
        order.setConsignTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        boolean success = orderMapper.updateById(order) > 0;
        log.info("发货{}", success ? "成功" : "失败");
        return success;
    }

    /**
     * 确认收货
     */
    @Override
    @Transactional
    public boolean confirmOrder(Long orderId, Long userId) {
        log.info("确认收货，订单ID: {}, 用户ID: {}", orderId, userId);
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            log.error("订单不存在或用户不匹配，订单ID: {}, 用户ID: {}", orderId, userId);
            return false;
        }

        // 只有待收货的订单可以确认收货
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_RECEIPT) {
            log.error("订单状态不允许确认收货，当前状态: {}", order.getStatus());
            return false;
        }

        order.setStatus(OrderConstant.ORDER_STATUS_COMPLETED);
        order.setEndTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        boolean success = orderMapper.updateById(order) > 0;
        log.info("确认收货{}", success ? "成功" : "失败");
        return success;
    }
    
    /**
     * 卖家确认收款
     */
    @Override
    @Transactional
    public boolean receivePayment(Long orderId, Long shopId) {
        log.info("卖家确认收款，订单ID: {}, 店铺ID: {}", orderId, shopId);
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getShopId().equals(shopId)) {
            log.error("订单不存在或店铺不匹配，订单ID: {}, 店铺ID: {}", orderId, shopId);
            return false;
        }

        // 只有待收货的订单可以确认收款（与买家确认收货是同一个状态）
        if (order.getStatus() != OrderConstant.ORDER_STATUS_PENDING_RECEIPT) {
            log.error("订单状态不允许确认收款，当前状态: {}", order.getStatus());
            return false;
        }

        // 确认收款后订单状态变为已完成
        order.setStatus(OrderConstant.ORDER_STATUS_COMPLETED);
        order.setEndTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        boolean success = orderMapper.updateById(order) > 0;
        log.info("卖家确认收款{}", success ? "成功" : "失败");
        return success;
    }

    /**
     * 获取商家订单列表
     */
    @Override
    public List<OrderDTO> getShopOrders(Long shopId) {
        log.info("获取商家订单列表，店铺ID: {}", shopId);
        return orderMapper.selectShopOrders(shopId);
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