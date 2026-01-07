import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auth/login',
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
