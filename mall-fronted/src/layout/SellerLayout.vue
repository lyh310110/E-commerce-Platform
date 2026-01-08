<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h3>卖家中心</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#E6A23C"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/seller">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/seller/products">
          <el-icon><Goods /></el-icon>
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/seller/orders">
          <el-icon><Document /></el-icon>
          <span>订单管理</span>
        </el-menu-item>
        <el-menu-item index="/seller/settings">
          <el-icon><Setting /></el-icon>
          <span>基础店铺设置</span>
        </el-menu-item>
        <el-menu-item index="/seller/coupons">
          <el-icon><Ticket /></el-icon>
          <span>优惠券管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="breadcrumb">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="user-info">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-icon><Avatar /></el-icon>
                {{ userInfo?.nickname || '卖家' }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Goods, Document, Setting, Ticket } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userInfo = ref(null)

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titleMap = {
    '/seller': '首页',
    '/seller/products': '商品管理',
    '/seller/orders': '订单管理',
    '/seller/settings': '基础店铺设置',
    '/seller/coupons': '优惠券管理'
  }
  return titleMap[route.path] || '首页'
})

const handleCommand = (command) => {
  if (command === 'logout') {
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('退出成功')
    router.push('/login')
  }
}

onMounted(() => {
  const userStr = localStorage.getItem('userInfo')
  if (userStr) {
    userInfo.value = JSON.parse(userStr)
  }
})
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

.aside {
  background-color: #E6A23C;
  overflow-x: hidden;
}

.logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  background-color: #CF9236;
}

.header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  padding: 0 20px;
}

.header-content {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 5px;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>