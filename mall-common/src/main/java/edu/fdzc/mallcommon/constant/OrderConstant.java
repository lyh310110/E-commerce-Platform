package edu.fdzc.mallcommon.constant;

public class OrderConstant {

    // 订单状态
    public static final int ORDER_STATUS_PENDING_PAYMENT = 1;    // 待付款
    public static final int ORDER_STATUS_PENDING_SHIPMENT = 2;   // 待发货
    public static final int ORDER_STATUS_PENDING_RECEIPT = 3;    // 待收货
    public static final int ORDER_STATUS_COMPLETED = 4;          // 已完成
    public static final int ORDER_STATUS_CANCELLED = 5;          // 已取消

    // 支付方式
    public static final int PAYMENT_TYPE_WECHAT = 1;    // 微信支付
    public static final int PAYMENT_TYPE_ALIPAY = 2;    // 支付宝

    // 店铺状态
    public static final int SHOP_STATUS_PENDING = 0;    // 待审核
    public static final int SHOP_STATUS_NORMAL = 1;     // 正常
    public static final int SHOP_STATUS_DISABLED = 2;   // 禁用

    // 商品状态
    public static final int PRODUCT_STATUS_ON_SALE = 1;   // 上架
    public static final int PRODUCT_STATUS_OFF_SALE = 0;  // 下架

    // 商品审核状态
    public static final int PRODUCT_AUDIT_PENDING = 0;    // 待审核
    public static final int PRODUCT_AUDIT_PASSED = 1;     // 审核通过
    public static final int PRODUCT_AUDIT_REJECTED = 2;   // 审核拒绝
}