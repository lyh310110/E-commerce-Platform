export const UserConstant = {
  USER_STATUS: {
    NORMAL: 1,
    DISABLED: 0
  },
  
  USER_ROLE: {
    BUYER: 1,
    SELLER: 2,
    ADMIN: 3
  },
  
  ROLE_STATUS: {
    NORMAL: 1,
    DISABLED: 0
  },
  
  SHOP_STATUS: {
    PENDING: 0,
    APPROVED: 1,
    REJECTED: 2
  },
  
  PRODUCT_STATUS: {
    PENDING: 0,
    ON_SHELF: 1,
    OFF_SHELF: 2
  },
  
  ORDER_STATUS: {
    PENDING: 0,
    PAID: 1,
    SHIPPED: 2,
    COMPLETED: 3,
    CANCELLED: 4
  }
}

export const UserStatusMap = {
  [UserConstant.USER_STATUS.NORMAL]: '正常',
  [UserConstant.USER_STATUS.DISABLED]: '禁用'
}

export const UserRoleMap = {
  [UserConstant.USER_ROLE.BUYER]: '买家',
  [UserConstant.USER_ROLE.SELLER]: '卖家',
  [UserConstant.USER_ROLE.ADMIN]: '管理员'
}

export const UserRoleTypeMap = {
  [UserConstant.USER_ROLE.BUYER]: 'success',
  [UserConstant.USER_ROLE.SELLER]: 'warning',
  [UserConstant.USER_ROLE.ADMIN]: 'danger'
}

export const ShopStatusMap = {
  [UserConstant.SHOP_STATUS.PENDING]: '待审核',
  [UserConstant.SHOP_STATUS.APPROVED]: '已通过',
  [UserConstant.SHOP_STATUS.REJECTED]: '已拒绝'
}

export const ShopStatusTypeMap = {
  [UserConstant.SHOP_STATUS.PENDING]: 'warning',
  [UserConstant.SHOP_STATUS.APPROVED]: 'success',
  [UserConstant.SHOP_STATUS.REJECTED]: 'danger'
}

export const ProductStatusMap = {
  [UserConstant.PRODUCT_STATUS.PENDING]: '待审核',
  [UserConstant.PRODUCT_STATUS.ON_SHELF]: '已上架',
  [UserConstant.PRODUCT_STATUS.OFF_SHELF]: '已下架'
}

export const ProductStatusTypeMap = {
  [UserConstant.PRODUCT_STATUS.PENDING]: 'warning',
  [UserConstant.PRODUCT_STATUS.ON_SHELF]: 'success',
  [UserConstant.PRODUCT_STATUS.OFF_SHELF]: 'danger'
}

export const OrderStatusMap = {
  [UserConstant.ORDER_STATUS.PENDING]: '待支付',
  [UserConstant.ORDER_STATUS.PAID]: '已支付',
  [UserConstant.ORDER_STATUS.SHIPPED]: '已发货',
  [UserConstant.ORDER_STATUS.COMPLETED]: '已完成',
  [UserConstant.ORDER_STATUS.CANCELLED]: '已取消'
}
