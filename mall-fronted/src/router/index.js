import { createRouter, createWebHistory } from 'vue-router'
import { UserConstant } from '@/constants'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue')
  },
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/buyer',
    name: 'Buyer',
    component: () => import('@/layout/BuyerLayout.vue'),
    redirect: '/buyer',
    children: [
      {
        path: '',
        name: 'BuyerHome',
        component: () => import('@/views/buyer/Home.vue')
      },
      {
        path: 'orders',
        name: 'BuyerOrders',
        component: () => import('@/views/buyer/Orders.vue')
      },
      {        path: 'products',        name: 'BuyerProducts',        component: () => import('@/views/buyer/Products.vue')      },      {        path: 'products/:id',        name: 'BuyerProductDetail',        component: () => import('@/views/buyer/ProductDetail.vue')      },
      {        path: 'favorites',        name: 'BuyerFavorites',        component: () => import('@/views/buyer/Favorites.vue')      },      {        path: 'footprints',        name: 'BuyerFootprints',        component: () => import('@/views/buyer/Footprints.vue')      },      {        path: 'mypage',        name: 'BuyerMyPage',        component: () => import('@/views/buyer/MyPage.vue')      },      {        path: 'coupons',        name: 'BuyerCoupons',        component: () => import('@/views/buyer/Coupons.vue')      }
    ]
  },
  {
    path: '/seller',
    name: 'Seller',
    component: () => import('@/layout/SellerLayout.vue'),
    redirect: '/seller',
    children: [
      {
        path: '',
        name: 'SellerHome',
        component: () => import('@/views/seller/Home.vue')
      },
      {
        path: 'products',
        name: 'SellerProducts',
        component: () => import('@/views/seller/ProductManagement.vue')
      },
      {
        path: 'orders',
        name: 'SellerOrders',
        component: () => import('@/views/seller/OrderManagement.vue')
      },
      {
        path: 'settings',
        name: 'SellerSettings',
        component: () => import('@/views/seller/ShopSettings.vue')
      },
      {
        path: 'coupons',
        name: 'SellerCoupons',
        component: () => import('@/views/seller/CouponManagement.vue')
      }
    ]
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/layout/AdminLayout.vue'),
    redirect: '/dashboard/home',
    children: [
      {
        path: 'home',
        name: 'Home',
        component: () => import('@/views/admin/Home.vue')
      },
      {
        path: 'users',
        name: 'Users',
        component: () => import('@/views/admin/UserManagement.vue')
      },
      {
        path: 'shops',
        name: 'Shops',
        component: () => import('@/views/admin/ShopManagement.vue')
      },
      {
        path: 'products',
        name: 'Products',
        component: () => import('@/views/admin/ProductManagement.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path !== '/login' && token) {
    const userInfo = localStorage.getItem('userInfo')
    if (userInfo) {
      const user = JSON.parse(userInfo)
      const userRole = user.role_type
      
      if (to.path.startsWith('/dashboard') && userRole !== UserConstant.USER_ROLE.ADMIN) {
        next('/login')
        return
      }
      if (to.path.startsWith('/buyer') && userRole !== UserConstant.USER_ROLE.BUYER) {
        next('/login')
        return
      }
      if (to.path.startsWith('/seller') && userRole !== UserConstant.USER_ROLE.SELLER) {
        next('/login')
        return
      }
    }
    next()
  } else {
    next()
  }
})

export default router
