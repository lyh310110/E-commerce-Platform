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
    redirect: '/dashboard'
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
      if (user.role_type !== UserConstant.USER_ROLE.ADMIN) {
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
