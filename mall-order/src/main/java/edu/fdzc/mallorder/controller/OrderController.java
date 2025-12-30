package edu.fdzc.mallorder.controller;

import edu.fdzc.mallcommon.entity.Result;
import edu.fdzc.mallorder.entity.Order;
import edu.fdzc.mallorder.entity.OrderItem;
import edu.fdzc.mallorder.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Map<String, Object> params) {
        try {
            // 从params中解析order
            Map<String, Object> orderMap = (Map<String, Object>) params.get("order");
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
            List<OrderItem> orderItems = new java.util.ArrayList<>();

            for (Map<String, Object> itemMap : itemsMap) {
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(Long.valueOf(itemMap.get("productId").toString()));
                orderItem.setProductName(itemMap.get("productName").toString());
                orderItem.setPrice(new java.math.BigDecimal(itemMap.get("price").toString()));
                orderItem.setQuantity(Integer.valueOf(itemMap.get("quantity").toString()));
                orderItem.setTotalPrice(new java.math.BigDecimal(itemMap.get("totalPrice").toString()));
                orderItems.add(orderItem);
            }

            Order createdOrder = orderService.createOrder(order, orderItems);
            return Result.success(createdOrder);

        } catch (Exception e) {
            return Result.error("订单创建失败：" + e.getMessage());
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
}