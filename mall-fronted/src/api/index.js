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

export function getProductList() {
  return request({
    url: '/product/list',
    method: 'get'
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
