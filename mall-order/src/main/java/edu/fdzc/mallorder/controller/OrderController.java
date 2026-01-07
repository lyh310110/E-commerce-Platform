package edu.fdzc.mallorder.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallorder.dto.OrderDTO;
import edu.fdzc.mallorder.entity.Order;
import edu.fdzc.mallorder.entity.OrderItem;
import edu.fdzc.mallorder.entity.Shop;
import edu.fdzc.mallorder.feign.UserServiceFeign;
import edu.fdzc.mallorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final UserServiceFeign userServiceFeign;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Map<String, Object> params) {
        try {
            log.info("开始创建订单，请求参数：{}", params);
            
            // 检查参数是否为空
            if (params == null) {
                log.error("请求参数为空");
                return Result.error("请求参数为空");
            }
            
            // 从params中解析order
            Map<String, Object> orderMap = (Map<String, Object>) params.get("order");
            if (orderMap == null) {
                log.error("订单信息为空");
                return Result.error("订单信息为空");
            }
            log.info("解析到的订单信息：{}", orderMap);
            
            // 验证orderMap中的必要字段
            checkOrderMapFields(orderMap);
            
            Order order = new Order();
            order.setUserId(Long.valueOf(orderMap.get("userId").toString()));
            order.setShopId(Long.valueOf(orderMap.get("shopId").toString()));
            order.setTotalAmount(new java.math.BigDecimal(orderMap.get("totalAmount").toString()));
            order.setPayAmount(new java.math.BigDecimal(orderMap.get("payAmount").toString()));
            order.setReceiverName(orderMap.get("receiverName").toString());
            order.setReceiverPhone(orderMap.get("receiverPhone").toString());
            order.setReceiverAddress(orderMap.get("receiverAddress").toString());

            // 从params中解析orderItems
            List<Map<String, Object>> itemsMap = (List<Map<String, Object>>) params.get("orderItems");
            if (itemsMap == null || itemsMap.isEmpty()) {
                log.error("订单项为空");
                return Result.error("订单项为空");
            }
            log.info("解析到的订单项数量：{}", itemsMap.size());
            
            List<OrderItem> orderItems = new java.util.ArrayList<>();

            for (Map<String, Object> itemMap : itemsMap) {
                if (itemMap == null) {
                    log.error("订单项为空");
                    continue;
                }
                
                // 验证itemMap中的必要字段
                checkOrderItemMapFields(itemMap);
                
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(Long.valueOf(itemMap.get("productId").toString()));
                orderItem.setProductName(itemMap.get("productName").toString());
                orderItem.setPrice(new java.math.BigDecimal(itemMap.get("price").toString()));
                orderItem.setQuantity(Integer.valueOf(itemMap.get("quantity").toString()));
                orderItem.setTotalPrice(new java.math.BigDecimal(itemMap.get("totalPrice").toString()));
                orderItems.add(orderItem);
                log.info("添加订单项：{}", orderItem);
            }

            // 再次检查订单项是否为空
            if (orderItems.isEmpty()) {
                log.error("有效订单项为空");
                return Result.error("有效订单项为空");
            }

            Order createdOrder = orderService.createOrder(order, orderItems);
            log.info("订单创建成功，订单信息：{}", createdOrder);
            
            return Result.success(createdOrder);

        } catch (NumberFormatException e) {
            log.error("订单创建失败：数字格式错误", e);
            return Result.error("订单创建失败：参数格式错误");
        } catch (NullPointerException e) {
            log.error("订单创建失败：空指针异常", e);
            return Result.error("订单创建失败：缺少必要参数");
        } catch (ClassCastException e) {
            log.error("订单创建失败：类型转换错误", e);
            return Result.error("订单创建失败：参数类型错误");
        } catch (Exception e) {
            log.error("订单创建失败：", e);
            String errorMessage = e.getMessage() != null ? e.getMessage() : "未知错误";
            return Result.error("订单创建失败：" + errorMessage);
        }
    }
    
    /**
     * 验证订单信息中的必要字段
     */
    private void checkOrderMapFields(Map<String, Object> orderMap) {
        String[] requiredFields = {"userId", "shopId", "totalAmount", "payAmount", "receiverName", "receiverPhone", "receiverAddress"};
        for (String field : requiredFields) {
            if (orderMap.get(field) == null) {
                log.error("订单信息缺少必要字段：{}", field);
                throw new IllegalArgumentException("订单信息缺少必要字段：" + field);
            }
        }
    }
    
    /**
     * 验证订单项信息中的必要字段
     */
    private void checkOrderItemMapFields(Map<String, Object> itemMap) {
        String[] requiredFields = {"productId", "productName", "price", "quantity", "totalPrice"};
        for (String field : requiredFields) {
            if (itemMap.get(field) == null) {
                log.error("订单项缺少必要字段：{}", field);
                throw new IllegalArgumentException("订单项缺少必要字段：" + field);
            }
        }
    }

    /**
     * 获取用户订单列表
     */
    @GetMapping("/list")
    public Result<List<Order>> getUserOrders(
            @RequestParam Long userId,
            @RequestParam(required = false) Integer status) {
        List<Order> orders = orderService.getUserOrders(userId, status);
        return Result.success(orders);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        return order != null ? Result.success(order) : Result.error("订单不存在");
    }

    /**
     * 获取订单商品列表
     */
    @GetMapping("/{id}/items")
    public Result<List<OrderItem>> getOrderItems(@PathVariable Long id) {
        List<OrderItem> items = orderService.getOrderItems(id);
        return Result.success(items);
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public Result<Boolean> cancelOrder(@PathVariable Long id,
                                       @RequestParam Long userId) {
        boolean success = orderService.cancelOrder(id, userId);
        return success ? Result.success() : Result.error("取消订单失败");
    }

    /**
     * 支付订单
     */
    @PostMapping("/{id}/pay")
    public Result<Boolean> payOrder(@PathVariable Long id,
                                    @RequestParam Integer paymentType) {
        boolean success = orderService.payOrder(id, paymentType);
        return success ? Result.success() : Result.error("支付失败");
    }

    /**
     * 发货
     */
    @PostMapping("/{id}/ship")
    public Result<Boolean> shipOrder(@PathVariable Long id,
                                     @RequestParam Long shopId) {
        boolean success = orderService.shipOrder(id, shopId);
        return success ? Result.success() : Result.error("发货失败");
    }

    /**
     * 确认收货
     */
    @PostMapping("/{id}/confirm")
    public Result<Boolean> confirmOrder(@PathVariable Long id,
                                        @RequestParam Long userId) {
        boolean success = orderService.confirmOrder(id, userId);
        return success ? Result.success() : Result.error("确认收货失败");
    }
    
    /**
     * 卖家确认收款
     */
    @PostMapping("/{id}/receive-payment")
    public Result<Boolean> receivePayment(@PathVariable Long id,
                                          @RequestParam Long shopId) {
        boolean success = orderService.receivePayment(id, shopId);
        return success ? Result.success() : Result.error("确认收款失败");
    }
    
    /**
     * 获取商家订单列表
     */
    @GetMapping("/seller/order/list")
    public Result<List<OrderDTO>> getShopOrders(@RequestParam(required = false) Long userId, 
                                               @RequestParam(required = false) Long shopId) {
        Long finalShopId = null;
        
        // 优先使用shopId
        if (shopId != null) {
            finalShopId = shopId;
        } else if (userId != null) {
            // 通过userId获取shopId
            try {
                Result<Shop> shopResult = userServiceFeign.getShopInfo(userId);
                if (shopResult.getCode() == 200 && shopResult.getData() != null) {
                    finalShopId = shopResult.getData().getId();
                    log.info("通过用户ID {} 获取到店铺ID: {}", userId, finalShopId);
                } else {
                    log.error("获取店铺信息失败: {}", shopResult.getMsg());
                    return Result.error("获取店铺信息失败");
                }
            } catch (Exception e) {
                log.error("调用用户服务获取店铺信息失败: {}", e.getMessage(), e);
                return Result.error("系统异常，请稍后重试");
            }
        } else {
            log.error("获取商家订单列表失败: 缺少必要参数userId或shopId");
            return Result.error("缺少必要参数");
        }
        
        List<OrderDTO> orders = orderService.getShopOrders(finalShopId);
        log.info("获取商家订单列表成功，店铺ID: {}, 订单数量: {}", finalShopId, orders.size());
        return Result.success(orders);
    }
}