<template>
  <el-container class="layout-container">
    <el-aside width="200px" class="aside">
      <div class="logo">
        <h3>买家中心</h3>
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#67C23A"
        text-color="#fff"
        active-text-color="#409EFF"
      >
        <el-menu-item index="/buyer">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/buyer/orders">
          <el-icon><List /></el-icon>
          <span>我的订单</span>
        </el-menu-item>
        <el-menu-item index="/buyer/products">
          <el-icon><Goods /></el-icon>
          <span>商品浏览</span>
        </el-menu-item>
        <el-menu-item index="/buyer/favorites">
          <el-icon><Setting /></el-icon>
          <span>设置</span>
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
                {{ userInfo?.nickname || '买家' }}
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

const router = useRouter()
const route = useRoute()
const userInfo = ref(null)

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titleMap = {
    '/buyer': '首页',
    '/buyer/orders': '我的订单',
    '/buyer/products': '商品浏览',
    '/buyer/favorites': '设置'
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
  background-color: #67C23A;
  overflow-x: hidden;
}

.logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  color: #fff;
  font-size: 16px;
  background-color: #5BA424;
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