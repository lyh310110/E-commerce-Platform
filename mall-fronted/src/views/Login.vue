<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>商城管理系统</h2>
        </div>
      </template>
      
      <el-tabs v-model="activeTab" type="border-card" class="login-tabs">
        <!-- 登录标签页 -->
        <el-tab-pane label="登录" name="login">
          <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef">
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
        </el-tab-pane>
        
        <!-- 注册标签页 -->
        <el-tab-pane label="注册" name="register">
          <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef">
            <el-form-item prop="username">
              <el-input
                v-model="registerForm.username"
                placeholder="请输入用户名"
                prefix-icon="User"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item prop="confirmPassword">
              <el-input
                v-model="registerForm.confirmPassword"
                type="password"
                placeholder="请确认密码"
                prefix-icon="Lock"
                size="large"
                show-password
              />
            </el-form-item>
            <el-form-item prop="nickname">
              <el-input
                v-model="registerForm.nickname"
                placeholder="请输入昵称"
                prefix-icon="ChatDotRound"
                size="large"
              />
            </el-form-item>
            <el-form-item prop="roleType" label="注册身份" class="role-type-item">
              <el-radio-group v-model="registerForm.roleType">
                <el-radio label="buyer">买家</el-radio>
                <el-radio label="seller">卖家</el-radio>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button
                type="primary"
                size="large"
                style="width: 100%"
                :loading="loading"
                @click="handleRegister"
              >
                注册
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register, registerSeller } from '@/api'
import { UserConstant } from '@/constants'

const router = useRouter()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const activeTab = ref('login')

const loginForm = reactive({
  username: '',
  password: ''
})

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  nickname: '',
  roleType: 'buyer' // 默认买家
})

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.password) {
          callback(new Error('两次输入密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  nickname: [
    { required: true, message: '请输入昵称', trigger: 'blur' },
    { min: 2, max: 20, message: '昵称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  roleType: [
    { required: true, message: '请选择注册身份', trigger: 'change' }
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
        ElMessage.error('登录失败，请检查用户名和密码')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  await registerFormRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const { roleType, ...registerData } = registerForm
        
        let res
        if (roleType === 'buyer') {
          res = await register(registerData)
        } else {
          res = await registerSeller(registerData)
        }
        
        if (res.code === 200) {
          // 注册成功后自动登录
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
            default:
              ElMessage.error('未知用户角色，无法访问系统')
              return
          }
          
          ElMessage.success(`注册成功！欢迎，${roleName}！`)
          router.push(targetPath)
        } else {
          ElMessage.error(res.message || '注册失败')
        }
      } catch (error) {
        console.error('注册失败:', error)
        // 显示后端返回的具体错误信息
        const errorMsg = error.message || '注册失败，请稍后重试'
        ElMessage.error(errorMsg)
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

.login-tabs {
  margin-top: 20px;
}

.role-type-item {
  margin-bottom: 16px;
}

.role-type-item .el-form-item__label {
  margin-right: 16px;
}
</style>
