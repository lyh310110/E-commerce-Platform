<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>商城管理系统</h2>
        </div>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
            size="large"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            size="large"
            show-password
            @keyup.enter="handleLogin"
          />
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            size="large"
            style="width: 100%"
            :loading="loading"
            @click="handleLogin"
          >
            登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api'
import { UserConstant } from '@/constants'

const router = useRouter()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  await loginFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const res = await login(loginForm)
        if (res.code === 200) {
          const user = res.data.user
          const roleType = res.data.roleType
          
          localStorage.setItem('token', res.data.token)
          localStorage.setItem('userInfo', JSON.stringify({ ...user, role_type: roleType }))
          
          let targetPath = ''
          let roleName = ''
          
          switch (roleType) {
            case UserConstant.USER_ROLE.BUYER:
              targetPath = '/buyer'
              roleName = '买家'
              break
            case UserConstant.USER_ROLE.SELLER:
              targetPath = '/seller'
              roleName = '卖家'
              break
            case UserConstant.USER_ROLE.ADMIN:
              targetPath = '/dashboard'
              roleName = '管理员'
              break
            default:
              ElMessage.error('未知用户角色，无法访问系统')
              return
          }
          
          ElMessage.success(`欢迎，${roleName}！`)
          router.push(targetPath)
        }
      } catch (error) {
        console.error('登录失败:', error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
}

.card-header h2 {
  margin: 0;
  color: #333;
}
</style>
