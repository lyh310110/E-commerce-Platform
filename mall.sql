/*
 Navicat Premium Data Transfer

 Source Server         : MySQL
 Source Server Type    : MySQL
 Source Server Version : 80042 (8.0.42)
 Source Host           : localhost:3306
 Source Schema         : mall

 Target Server Type    : MySQL
 Target Server Version : 80042 (8.0.42)
 File Encoding         : 65001

 Date: 08/01/2026 21:34:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `num` int NOT NULL DEFAULT 1 COMMENT '数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2009223795438579715 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '购物车表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (2, 3, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (5, 7, 3, 'Apple iPhone 15', 7999.00, 1);
INSERT INTO `cart` VALUES (6, 7, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (7, 7, 3, 'Apple iPhone 15', 7999.00, 1);
INSERT INTO `cart` VALUES (8, 7, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (9, 7, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (10, 7, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (2008851838402617345, 4, 4, '测试商品1', 99.99, 1);
INSERT INTO `cart` VALUES (2008915549804855297, 4, 1, '小米14', 4999.00, 1);
INSERT INTO `cart` VALUES (2009223795438579714, 4, 2, '华为Mate60', 5999.00, 1);

-- ----------------------------
-- Table structure for coupon
-- ----------------------------
DROP TABLE IF EXISTS `coupon`;
CREATE TABLE `coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券描述',
  `type` tinyint NOT NULL,
  `value` decimal(10, 2) NOT NULL,
  `min_amount` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '使用门槛（满多少可使用）',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `total_quantity` int NOT NULL,
  `remaining_quantity` int NOT NULL,
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-可用 0-不可用',
  `shop_id` bigint NULL DEFAULT NULL COMMENT '店铺ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_start_time`(`start_time` ASC) USING BTREE,
  INDEX `idx_end_time`(`end_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '优惠券表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of coupon
-- ----------------------------
INSERT INTO `coupon` VALUES (1, '1', '1', 1, 0.01, 0.01, '2026-01-08 04:34:22', '2026-01-15 04:34:22', 100, 100, 1, 1);
INSERT INTO `coupon` VALUES (3, '2', '2', 1, 0.02, 0.02, '2026-01-08 11:18:11', '2026-01-15 11:18:11', 100, 100, 1, 4);

-- ----------------------------
-- Table structure for cs_reply
-- ----------------------------
DROP TABLE IF EXISTS `cs_reply`;
CREATE TABLE `cs_reply`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cs_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cs_reply
-- ----------------------------

-- ----------------------------
-- Table structure for customer_service
-- ----------------------------
DROP TABLE IF EXISTS `customer_service`;
CREATE TABLE `customer_service`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` int NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer_service
-- ----------------------------
INSERT INTO `customer_service` VALUES (1, 4, '12345', '1234561234', 0, '2026-01-07 14:51:24', '2026-01-07 14:51:24');

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `user_id` bigint NOT NULL COMMENT '买家用户ID',
  `shop_id` bigint NOT NULL COMMENT '店铺ID',
  `total_amount` decimal(10, 2) NOT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10, 2) NOT NULL COMMENT '实付金额',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '订单状态：1-待付款 2-待发货 3-待收货 4-已完成 5-已取消',
  `payment_type` tinyint NULL DEFAULT NULL COMMENT '支付方式：1-微信 2-支付宝',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `consign_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '交易完成时间',
  `close_time` datetime NULL DEFAULT NULL COMMENT '交易关闭时间',
  `receiver_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收货地址',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_shop_id`(`shop_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, '176707941882485c20c', 3, 6, 4999.00, 4999.00, 1, NULL, NULL, NULL, NULL, NULL, '张三', '13800138000', '北京市朝阳区测试街道123号', NULL, '2025-12-30 15:23:39', '2026-01-05 21:03:38');
INSERT INTO `order` VALUES (2, '17676110288119a855a', 1, 6, 100.00, 100.00, 1, NULL, NULL, NULL, NULL, NULL, '??', '13800138000', '??????', NULL, '2026-01-05 19:03:49', '2026-01-05 22:05:01');
INSERT INTO `order` VALUES (3, '176761115245497354a', 1, 1, 100.00, 100.00, 1, NULL, NULL, NULL, NULL, NULL, '??', '13800138000', '??????', NULL, '2026-01-05 19:05:52', '2026-01-05 19:05:52');
INSERT INTO `order` VALUES (4, '176761116903109eb93', 1, 1, 100.00, 100.00, 1, NULL, NULL, NULL, NULL, NULL, '??', '13800138000', '??????', NULL, '2026-01-05 19:06:09', '2026-01-05 19:06:09');
INSERT INTO `order` VALUES (5, '176761131188257b11e', 7, 1, 18997.00, 18997.00, 2, 1, '2026-01-06 04:27:27', NULL, NULL, '2026-01-06 03:19:52', 'yves', '13800138000', '默认收货地址', NULL, '2026-01-05 19:08:32', '2026-01-06 04:27:27');
INSERT INTO `order` VALUES (6, '1767613638720a41c2c', 7, 1, 5999.00, 5999.00, 1, 1, '2026-01-06 02:42:11', NULL, NULL, '2026-01-06 03:17:56', 'yves', '13800138000', '默认收货地址', NULL, '2026-01-05 19:47:19', '2026-01-06 03:20:06');
INSERT INTO `order` VALUES (7, '1767613638757145431', 7, 1, 4999.00, 4999.00, 5, 1, '2026-01-06 03:18:02', NULL, NULL, '2026-01-06 04:27:28', 'yves', '13800138000', '默认收货地址', NULL, '2026-01-05 19:47:19', '2026-01-06 04:27:28');
INSERT INTO `order` VALUES (8, '176764484187571ea48', 7, 1, 4999.00, 4999.00, 1, NULL, NULL, NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-06 04:27:22', '2026-01-06 04:27:22');
INSERT INTO `order` VALUES (13, '202601060001', 4, 4, 299.98, 299.98, 2, 1, '2026-01-07 17:25:47', NULL, NULL, NULL, '寮犱笁', '13800138000', '鍖椾含鏈濋槼', NULL, '2026-01-06 04:51:17', '2026-01-07 17:25:47');
INSERT INTO `order` VALUES (14, '202601060002', 1, 4, 1299.99, 1299.99, 3, NULL, NULL, '2026-01-06 06:16:53', '2026-01-06 06:05:53', NULL, '鏉庡洓', '13900139000', '涓婃捣娴︿笢', NULL, '2026-01-06 04:51:17', '2026-01-06 06:16:53');
INSERT INTO `order` VALUES (15, '202601060003', 4, 4, 99.99, 99.99, 4, NULL, NULL, '2026-01-06 06:16:54', '2026-01-07 17:25:49', NULL, '鐜嬩簲', '13700137000', '骞垮窞澶╂渤', NULL, '2026-01-06 04:51:17', '2026-01-07 17:25:49');
INSERT INTO `order` VALUES (16, '202601060004', 1, 4, 199.99, 199.99, 4, NULL, NULL, NULL, NULL, NULL, '璧靛叚', '13600136000', '娣卞湷鍗楀北', NULL, '2026-01-06 04:51:17', '2026-01-06 04:51:17');
INSERT INTO `order` VALUES (18, '176777786334538be79', 4, 1, 4999.00, 4999.00, 5, NULL, NULL, NULL, NULL, '2026-01-07 17:25:07', '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 17:24:23', '2026-01-07 17:25:07');
INSERT INTO `order` VALUES (19, '17677778742295fa5c1', 4, 1, 4999.00, 4999.00, 5, NULL, NULL, NULL, NULL, '2026-01-07 17:25:06', '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 17:24:34', '2026-01-07 17:25:06');
INSERT INTO `order` VALUES (20, '17677977481823aa299', 4, 1, 99.99, 99.99, 2, 1, '2026-01-07 22:56:25', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 22:55:48', '2026-01-07 22:56:25');
INSERT INTO `order` VALUES (21, '17677977555324d456f', 4, 1, 99.99, 99.99, 2, 1, '2026-01-07 22:56:28', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 22:55:56', '2026-01-07 22:56:28');
INSERT INTO `order` VALUES (22, '1767797771357babd3b', 4, 1, 99.99, 99.99, 2, 1, '2026-01-07 22:56:28', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 22:56:11', '2026-01-07 22:56:28');
INSERT INTO `order` VALUES (23, '176779777232808dcef', 4, 1, 4999.00, 4999.00, 2, 1, '2026-01-07 22:56:26', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-07 22:56:12', '2026-01-07 22:56:26');
INSERT INTO `order` VALUES (24, '1767871247074b60f91', 4, 1, 4999.00, 4999.00, 2, 1, '2026-01-08 19:21:03', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-08 19:20:47', '2026-01-08 19:21:03');
INSERT INTO `order` VALUES (25, '17678712505390fe8ec', 4, 1, 99.99, 99.99, 2, 1, '2026-01-08 19:21:02', NULL, NULL, NULL, '默认收货人', '13800138000', '默认收货地址', NULL, '2026-01-08 19:20:51', '2026-01-08 19:21:02');

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '订单编号',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `product_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `product_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价',
  `quantity` int NOT NULL COMMENT '购买数量',
  `total_price` decimal(10, 2) NOT NULL COMMENT '商品总价',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE,
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 25 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '订单商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES (1, 1, '176707941882485c20c', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2025-12-30 15:23:39');
INSERT INTO `order_item` VALUES (2, 2, '17676110288119a855a', 1, '????', NULL, 100.00, 1, 100.00, '2026-01-05 19:03:49');
INSERT INTO `order_item` VALUES (3, 3, '176761115245497354a', 1, '????', NULL, 50.00, 2, 100.00, '2026-01-05 19:05:52');
INSERT INTO `order_item` VALUES (4, 4, '176761116903109eb93', 1, '????', NULL, 50.00, 2, 100.00, '2026-01-05 19:06:09');
INSERT INTO `order_item` VALUES (5, 5, '176761131188257b11e', 2, '华为Mate60', NULL, 5999.00, 1, 5999.00, '2026-01-05 19:08:32');
INSERT INTO `order_item` VALUES (6, 5, '176761131188257b11e', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-05 19:08:32');
INSERT INTO `order_item` VALUES (7, 5, '176761131188257b11e', 3, 'Apple iPhone 15', NULL, 7999.00, 1, 7999.00, '2026-01-05 19:08:32');
INSERT INTO `order_item` VALUES (8, 6, '1767613638720a41c2c', 2, '华为Mate60', NULL, 5999.00, 1, 5999.00, '2026-01-05 19:47:19');
INSERT INTO `order_item` VALUES (9, 7, '1767613638757145431', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-05 19:47:19');
INSERT INTO `order_item` VALUES (10, 8, '176764484187571ea48', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-06 04:27:22');
INSERT INTO `order_item` VALUES (11, 13, '202601060001', 4, '??????1', NULL, 99.99, 1, 99.99, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (12, 13, '202601060001', 5, '??????2', NULL, 199.99, 1, 199.99, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (13, 14, '202601060002', 6, '??????3', NULL, 299.99, 3, 899.97, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (14, 14, '202601060002', 4, '??????1', NULL, 99.99, 1, 99.99, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (15, 15, '202601060003', 5, '??????2', NULL, 199.99, 1, 199.99, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (16, 16, '202601060004', 6, '??????3', NULL, 299.99, 1, 299.99, '2026-01-06 04:54:55');
INSERT INTO `order_item` VALUES (17, 18, '176777786334538be79', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-07 17:24:23');
INSERT INTO `order_item` VALUES (18, 19, '17677778742295fa5c1', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-07 17:24:34');
INSERT INTO `order_item` VALUES (19, 20, '17677977481823aa299', 4, '测试商品1', NULL, 99.99, 1, 99.99, '2026-01-07 22:55:48');
INSERT INTO `order_item` VALUES (20, 21, '17677977555324d456f', 4, '测试商品1', NULL, 99.99, 1, 99.99, '2026-01-07 22:55:56');
INSERT INTO `order_item` VALUES (21, 22, '1767797771357babd3b', 4, '测试商品1', NULL, 99.99, 1, 99.99, '2026-01-07 22:56:11');
INSERT INTO `order_item` VALUES (22, 23, '176779777232808dcef', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-07 22:56:12');
INSERT INTO `order_item` VALUES (23, 24, '1767871247074b60f91', 1, '小米14', NULL, 4999.00, 1, 4999.00, '2026-01-08 19:20:47');
INSERT INTO `order_item` VALUES (24, 25, '17678712505390fe8ec', 4, '测试商品1', NULL, 99.99, 1, 99.99, '2026-01-08 19:20:51');

-- ----------------------------
-- Table structure for point_record
-- ----------------------------
DROP TABLE IF EXISTS `point_record`;
CREATE TABLE `point_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `type` int NOT NULL,
  `amount` int NOT NULL,
  `balance` int NOT NULL,
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of point_record
-- ----------------------------
INSERT INTO `point_record` VALUES (1, 1, 1, 1, 1, '1', '2026-01-01 22:53:29');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '商品名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品描述',
  `price` decimal(10, 2) NOT NULL COMMENT '商品价格',
  `stock` int NOT NULL DEFAULT 0 COMMENT '库存',
  `image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '商品图片URL',
  `shop_id` bigint NOT NULL DEFAULT 0 COMMENT '店铺ID',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-上架 0-下架',
  `audit_status` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核 1-审核通过 2-审核拒绝',
  `category_id` bigint NULL DEFAULT NULL COMMENT '分类ID',
  `sale_count` int NOT NULL DEFAULT 0 COMMENT '销量',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (1, '小米14', '骁龙8 Gen3，2K直屏', 4999.00, 100, 'https://img11.360buyimg.com/n1/jfs/t1/215752/27/25464/169112/6522e103F7a08a0d8/80d91a59956a4a07.jpg', 1, 1, 0, NULL, 0, '2025-12-30 11:07:59', '2026-01-04 22:55:33');
INSERT INTO `product` VALUES (2, '华为Mate60', '麒麟9000S，卫星通信', 5999.00, 80, 'https://img14.360buyimg.com/n1/jfs/t1/199501/36/26449/202559/65099643F29040417/9a75999f0f53635d.jpg', 2, 1, 0, NULL, 0, '2025-12-30 11:07:59', '2026-01-04 22:55:17');
INSERT INTO `product` VALUES (3, 'Apple iPhone 15', 'A17 Pro，iOS 17', 7999.00, 50, 'https://img10.360buyimg.com/n1/jfs/t1/204425/22/25949/202533/6510d640F39068d0d/8b0a91968f4a3e0a.jpg', 3, 1, 0, NULL, 0, '2025-12-30 11:07:59', '2026-01-04 22:55:17');
INSERT INTO `product` VALUES (4, '测试商品1', '这是第一个测试商品', 99.99, 100, NULL, 4, 1, 1, NULL, 0, '2026-01-05 21:32:31', '2026-01-05 21:51:47');
INSERT INTO `product` VALUES (5, '测试商品2', '这是第二个测试商品', 199.99, 50, NULL, 4, 1, 1, NULL, 0, '2026-01-05 21:32:31', '2026-01-05 21:32:31');
INSERT INTO `product` VALUES (6, '测试商品3', '这是第三个测试商品', 299.99, 20, NULL, 4, 1, 1, NULL, 0, '2026-01-05 21:32:31', '2026-01-05 21:32:31');
INSERT INTO `product` VALUES (7, '测试商品4', '无', 999.99, 99, NULL, 4, 1, 0, NULL, 0, '2026-01-05 22:00:32', '2026-01-06 06:15:48');

-- ----------------------------
-- Table structure for shop
-- ----------------------------
DROP TABLE IF EXISTS `shop`;
CREATE TABLE `shop`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '关联用户ID（店主）',
  `shop_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '店铺名称',
  `shop_logo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺logo',
  `shop_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '店铺描述',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系电话',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-待审核 1-正常 2-禁用',
  `audit_remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_shop_name`(`shop_name` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '店铺表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of shop
-- ----------------------------
INSERT INTO `shop` VALUES (1, 5, '测试店铺', NULL, '这是一个测试店铺', '13800138001', 1, NULL, '2025-12-30 14:48:27', '2026-01-05 20:51:45');
INSERT INTO `shop` VALUES (2, 1, '小米店', NULL, '小米', NULL, 0, NULL, '2026-01-04 23:34:04', '2026-01-04 23:34:04');
INSERT INTO `shop` VALUES (4, 6, '测试店铺_6', NULL, '这是用户6的测试店铺', '13800138001', 1, NULL, '2026-01-05 21:31:46', '2026-01-06 06:14:20');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'student', '123456', '大学生', NULL, NULL, 1, '2026-01-07 17:10:05', '2025-12-30 11:07:59', '2025-12-30 11:07:59');
INSERT INTO `user` VALUES (2, 'lyh', '123456', 'lyh', NULL, NULL, 1, '2026-01-07 17:05:30', '2025-12-30 11:07:59', '2025-12-30 15:25:44');
INSERT INTO `user` VALUES (3, 'admin', '123456', '系统管理员', NULL, NULL, 1, '2026-01-08 18:30:20', '2025-12-30 11:07:59', '2025-12-30 11:07:59');
INSERT INTO `user` VALUES (4, 'buyer001', '123456', '测试买家', NULL, NULL, 1, '2026-01-08 21:31:17', '2025-12-30 14:43:58', '2025-12-30 14:43:58');
INSERT INTO `user` VALUES (5, 'newbuyer001', '123456', '新测试买家', NULL, NULL, 1, '2026-01-08 19:12:06', '2025-12-30 15:07:52', '2025-12-30 15:07:52');
INSERT INTO `user` VALUES (6, 'newseller001', '123456', '新测试卖家', NULL, NULL, 1, '2026-01-08 19:18:00', '2025-12-30 15:11:10', '2025-12-30 15:11:10');
INSERT INTO `user` VALUES (7, 'yves', '1234567', 'yves', NULL, NULL, 1, '2026-01-07 18:06:59', '2026-01-04 14:45:55', '2026-01-06 04:27:48');
INSERT INTO `user` VALUES (9, 'test', 'test', '测试用户', NULL, '13800138000', 1, NULL, '2026-01-08 15:51:41', '2026-01-08 15:51:41');
INSERT INTO `user` VALUES (10, 'testuser123', 'password123', '????', NULL, NULL, 1, '2026-01-08 21:26:07', '2026-01-08 21:26:07', '2026-01-08 21:26:07');
INSERT INTO `user` VALUES (11, 'testuser', '123456', '????', NULL, NULL, 1, '2026-01-08 21:32:47', '2026-01-08 21:32:47', '2026-01-08 21:32:47');
INSERT INTO `user` VALUES (12, 'test1', '123456', 'test1', NULL, NULL, 1, '2026-01-08 21:33:30', '2026-01-08 21:33:30', '2026-01-08 21:33:30');

-- ----------------------------
-- Table structure for user_coupon
-- ----------------------------
DROP TABLE IF EXISTS `user_coupon`;
CREATE TABLE `user_coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `coupon_id` bigint NOT NULL,
  `obtain_time` datetime NOT NULL,
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '0:未使用, 1:已使用, 2:已过期',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `coupon_id`(`coupon_id` ASC) USING BTREE,
  CONSTRAINT `user_coupon_ibfk_1` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_coupon
-- ----------------------------
INSERT INTO `user_coupon` VALUES (1, 4, 1, '2026-01-08 04:43:40', 0);
INSERT INTO `user_coupon` VALUES (2, 4, 1, '2026-01-08 07:46:39', 1);
INSERT INTO `user_coupon` VALUES (3, 4, 1, '2026-01-08 07:46:41', 1);
INSERT INTO `user_coupon` VALUES (4, 4, 1, '2026-01-08 08:00:09', 1);
INSERT INTO `user_coupon` VALUES (5, 4, 3, '2026-01-08 11:18:40', 1);
INSERT INTO `user_coupon` VALUES (6, 4, 3, '2026-01-08 11:18:52', 1);

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_product`(`user_id` ASC, `product_id` ASC) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户收藏表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_favorite
-- ----------------------------
INSERT INTO `user_favorite` VALUES (1, 1, 1, '2026-01-07 22:31:12');
INSERT INTO `user_favorite` VALUES (15, 4, 5, '2026-01-08 03:03:14');
INSERT INTO `user_favorite` VALUES (16, 4, 6, '2026-01-08 03:03:17');
INSERT INTO `user_favorite` VALUES (17, 4, 7, '2026-01-08 03:03:22');
INSERT INTO `user_favorite` VALUES (18, 4, 1, '2026-01-08 03:03:23');
INSERT INTO `user_favorite` VALUES (19, 4, 2, '2026-01-08 03:03:24');
INSERT INTO `user_favorite` VALUES (20, 4, 3, '2026-01-08 03:03:25');
INSERT INTO `user_favorite` VALUES (21, 4, 4, '2026-01-08 03:36:44');

-- ----------------------------
-- Table structure for user_footprint
-- ----------------------------
DROP TABLE IF EXISTS `user_footprint`;
CREATE TABLE `user_footprint`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '足迹ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `product_id` bigint NOT NULL COMMENT '商品ID',
  `visit_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_product_id`(`product_id` ASC) USING BTREE,
  INDEX `idx_visit_time`(`visit_time` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户足迹表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_footprint
-- ----------------------------
INSERT INTO `user_footprint` VALUES (7, 4, 4, '2026-01-08 04:06:56');
INSERT INTO `user_footprint` VALUES (8, 4, 5, '2026-01-08 03:37:01');

-- ----------------------------
-- Table structure for user_point
-- ----------------------------
DROP TABLE IF EXISTS `user_point`;
CREATE TABLE `user_point`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `total_points` int NOT NULL,
  `available_points` int NOT NULL,
  `used_points` int NOT NULL,
  `expired_points` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_point
-- ----------------------------
INSERT INTO `user_point` VALUES (1, 1, 0, 0, 0, 0);
INSERT INTO `user_point` VALUES (2, 4, 0, 0, 0, 0);
INSERT INTO `user_point` VALUES (3, 12, 0, 0, 0, 0);

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_type` tinyint NOT NULL COMMENT '角色类型：1-买家 2-卖家 3-管理员',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：1-正常 0-禁用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE,
  INDEX `idx_role_type`(`role_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (1, 4, 1, 1, '2025-12-30 14:43:58', '2025-12-30 14:43:58');
INSERT INTO `user_role` VALUES (2, 5, 1, 1, '2025-12-30 15:07:52', '2026-01-04 23:52:41');
INSERT INTO `user_role` VALUES (3, 6, 2, 1, '2025-12-30 15:11:10', '2025-12-30 15:11:10');
INSERT INTO `user_role` VALUES (4, 7, 1, 1, '2026-01-04 14:45:55', '2026-01-04 14:45:55');
INSERT INTO `user_role` VALUES (5, 3, 3, 1, '2026-01-04 22:25:10', '2026-01-04 23:52:34');
INSERT INTO `user_role` VALUES (6, 8, 1, 1, '2026-01-08 10:24:56', '2026-01-08 10:24:56');
INSERT INTO `user_role` VALUES (7, 9, 1, 1, '2026-01-08 15:51:52', '2026-01-08 15:51:52');
INSERT INTO `user_role` VALUES (8, 10, 1, 1, '2026-01-08 21:26:07', '2026-01-08 21:26:07');
INSERT INTO `user_role` VALUES (9, 11, 1, 1, '2026-01-08 21:32:47', '2026-01-08 21:32:47');
INSERT INTO `user_role` VALUES (10, 12, 1, 1, '2026-01-08 21:33:30', '2026-01-08 21:33:30');

SET FOREIGN_KEY_CHECKS = 1;
