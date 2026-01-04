import {
  UserStatusMap,
  UserRoleMap,
  UserRoleTypeMap,
  ShopStatusMap,
  ShopStatusTypeMap,
  ProductStatusMap,
  ProductStatusTypeMap,
  OrderStatusMap,
  UserConstant
} from '@/constants'

export function getUserStatusText(status) {
  return UserStatusMap[status] || '未知'
}

export function getUserRoleText(role) {
  return UserRoleMap[role] || '未知'
}

export function getUserRoleType(role) {
  return UserRoleTypeMap[role] || 'info'
}

export function getShopStatusText(status) {
  return ShopStatusMap[status] || '未知'
}

export function getShopStatusType(status) {
  return ShopStatusTypeMap[status] || 'info'
}

export function getProductStatusText(status) {
  return ProductStatusMap[status] || '未知'
}

export function getProductStatusType(status) {
  return ProductStatusTypeMap[status] || 'info'
}

export function getOrderStatusText(status) {
  return OrderStatusMap[status] || '未知'
}

export function isUserNormal(status) {
  return status === UserConstant.USER_STATUS.NORMAL
}

export function isUserDisabled(status) {
  return status === UserConstant.USER_STATUS.DISABLED
}

export function isBuyer(role) {
  return role === UserConstant.USER_ROLE.BUYER
}

export function isSeller(role) {
  return role === UserConstant.USER_ROLE.SELLER
}

export function isAdmin(role) {
  return role === UserConstant.USER_ROLE.ADMIN
}

export function isShopPending(status) {
  return status === UserConstant.SHOP_STATUS.PENDING
}

export function isShopApproved(status) {
  return status === UserConstant.SHOP_STATUS.APPROVED
}

export function isShopRejected(status) {
  return status === UserConstant.SHOP_STATUS.REJECTED
}

export function isProductPending(status) {
  return status === UserConstant.PRODUCT_STATUS.PENDING
}

export function isProductOnShelf(status) {
  return status === UserConstant.PRODUCT_STATUS.ON_SHELF
}

export function isProductOffShelf(status) {
  return status === UserConstant.PRODUCT_STATUS.OFF_SHELF
}

export function canDisableUser(status) {
  return isUserNormal(status)
}

export function canEnableUser(status) {
  return isUserDisabled(status)
}

export function canApproveShop(status) {
  return isShopPending(status) || isShopRejected(status)
}

export function canRejectShop(status) {
  return isShopPending(status) || isShopApproved(status)
}

export function canApproveProduct(status) {
  return isProductPending(status)
}

export function canOffShelfProduct(status) {
  return isProductOnShelf(status)
}

export function getUserRoleOptions() {
  return [
    { label: '买家', value: UserConstant.USER_ROLE.BUYER },
    { label: '卖家', value: UserConstant.USER_ROLE.SELLER },
    { label: '管理员', value: UserConstant.USER_ROLE.ADMIN }
  ]
}

export function getUserStatusOptions() {
  return [
    { label: '正常', value: UserConstant.USER_STATUS.NORMAL },
    { label: '禁用', value: UserConstant.USER_STATUS.DISABLED }
  ]
}

export function getShopStatusOptions() {
  return [
    { label: '待审核', value: UserConstant.SHOP_STATUS.PENDING },
    { label: '已通过', value: UserConstant.SHOP_STATUS.APPROVED },
    { label: '已拒绝', value: UserConstant.SHOP_STATUS.REJECTED }
  ]
}

export function getProductStatusOptions() {
  return [
    { label: '待审核', value: UserConstant.PRODUCT_STATUS.PENDING },
    { label: '已上架', value: UserConstant.PRODUCT_STATUS.ON_SHELF },
    { label: '已下架', value: UserConstant.PRODUCT_STATUS.OFF_SHELF }
  ]
}
