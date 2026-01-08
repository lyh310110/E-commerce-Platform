import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
    method: 'post',
    data
  })
}

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function registerSeller(data) {
  return request({
    url: '/auth/register/seller',
    method: 'post',
    data
  })
}

export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

export function getShopInfo(userId) {
  return request({
    url: '/shop/info',
    method: 'get',
    params: { userId }
  })
}

export function getShopList(params) {
  return request({
    url: '/shop/list',
    method: 'get',
    params
  })
}

export function updateShop(data) {
  return request({
    url: '/shop/update',
    method: 'put',
    data
  })
}

export function getProductList(params) {
  return request({
    url: '/product/list',
    method: 'get',
    params
  })
}

export function getShopProducts(params) {
  return request({
    url: '/seller/product/list',
    method: 'get',
    params
  })
}

export function getProductById(id) {
  return request({
    url: `/product/${id}`,
    method: 'get'
  })
}

export function getProductComments(id) {
  return request({
    url: `/product/${id}/comments`,
    method: 'get'
  })
}

export function updateProduct(data) {
  return request({
    url: '/seller/product/update',
    method: 'put',
    data
  })
}

export function offShelfProduct(id, shopId) {
  return request({
    url: `/seller/product/off-shelf/${id}`,
    method: 'post',
    params: { shopId }
  })
}

export function addProduct(data) {
  return request({
    url: '/seller/product/publish',
    method: 'post',
    data
  })
}

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function getOrderDetail(id) {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export function getOrderDetailWithItems(id) {
  return request({
    url: `/order/${id}/detail`,
    method: 'get'
  })
}

export function getCartList(userId) {
  return request({
    url: '/cart/list',
    method: 'get',
    params: { userId }
  })
}

export function addToCart(data) {
  return request({
    url: '/cart/add',
    method: 'post',
    data
  })
}

export function removeFromCart(id) {
  return request({
    url: `/cart/${id}`,
    method: 'delete'
  })
}

export function createOrder(data) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

export function payOrder(id) {
  return request({
    url: `/order/${id}/pay`,
    method: 'post',
    params: { paymentType: 1 } // 默认支付方式
  })
}

export function confirmOrder(id, userId) {
  return request({
    url: `/order/${id}/confirm`,
    method: 'post',
    params: { userId }
  })
}

export function cancelOrder(id, userId) {
  return request({
    url: `/order/${id}/cancel`,
    method: 'post',
    params: { userId },
    headers: {
      'Content-Type': 'application/json'
    }
  })
}

export function getShopOrders(params) {
  return request({
    url: '/order/seller/order/list',
    method: 'get',
    params
  })
}

export function shipOrder(id, shopId) {
  return request({
    url: `/order/${id}/ship`,
    method: 'post',
    params: { shopId }
  })
}

export function getDashboardStatistics() {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

export function register(data) {
  return request({
    url: '/auth/register',
    method: 'post',
    data
  })
}

export function registerSeller(data) {
  return request({
    url: '/auth/register/seller',
    method: 'post',
    data
  })
}

export function receivePayment(id, shopId) {
  return request({
    url: `/order/${id}/receive-payment`,
    method: 'post',
    params: { shopId }
  })
}

export function getDashboardStatistics() {
  return request({
    url: '/statistics/dashboard',
    method: 'get'
  })
}

// 收藏功能相关API
export function getFavorites(userId) {
  return request({
    url: '/favorite/list',
    method: 'get',
    params: { userId }
  })
}

export function addFavorite(data) {
  return request({
    url: '/favorite/add',
    method: 'post',
    data
  })
}

export function removeFavorite(id) {
  return request({
    url: `/favorite/${id}`,
    method: 'delete'
  })
}

export function checkFavorite(userId, productId) {
  return request({
    url: '/favorite/check',
    method: 'get',
    params: { userId, productId }
  })
}

// 足迹功能相关API
export function getFootprints(userId) {
  return request({
    url: '/footprint/list',
    method: 'get',
    params: { userId }
  })
}

export function addFootprint(data) {
  return request({
    url: '/footprint/add',
    method: 'post',
    data
  })
}

export function clearFootprints(userId) {
  return request({
    url: '/footprint/clear',
    method: 'delete',
    params: { userId }
  })
}

// 优惠券功能相关API
export function getAvailableCoupons() {
  return request({
    url: '/coupon/available',
    method: 'get'
  })
}

export function getUserCoupons(userId) {
  return request({
    url: '/coupon/user/list',
    method: 'get',
    params: { userId }
  })
}

export function obtainCoupon(userId, couponId) {
  return request({
    url: '/coupon/obtain',
    method: 'post',
    params: { userId, couponId }
  })
}

// 卖家端优惠券管理API
export function getSellerCoupons(shopId) {
  return request({
    url: '/coupon/seller/list',
    method: 'get',
    params: { shopId }
  })
}

export function createCoupon(data) {
  return request({
    url: '/coupon/seller/create',
    method: 'post',
    data
  })
}

export function updateCoupon(data) {
  return request({
    url: '/coupon/seller/update',
    method: 'post',
    data
  })
}

export function deleteCoupon(id) {
  return request({
    url: '/coupon/seller/delete',
    method: 'post',
    params: { id }
  })
}

export function toggleCouponStatus(id, status) {
  return request({
    url: '/coupon/seller/toggle-status',
    method: 'post',
    params: { id, status }
  })
}

// 积分系统相关API
export function getUserPoint(userId) {
  return request({
    url: '/point/user',
    method: 'get',
    params: { userId }
  })
}

export function getPointRecords(userId) {
  return request({
    url: '/point/records',
    method: 'get',
    params: { userId }
  })
}

// 客服咨询相关API
export function getConsultations(userId) {
  return request({
    url: '/customer-service/list',
    method: 'get',
    params: { userId }
  })
}

export function getConsultationDetail(id) {
  return request({
    url: `/customer-service/detail/${id}`,
    method: 'get'
  })
}

export function getConsultationReplies(consultationId) {
  return request({
    url: `/customer-service/replies/${consultationId}`,
    method: 'get'
  })
}

export function addConsultation(data) {
  return request({
    url: '/customer-service/add',
    method: 'post',
    data
  })
}

export function addReply(data) {
  return request({
    url: '/customer-service/reply/add',
    method: 'post',
    data
  })
}
