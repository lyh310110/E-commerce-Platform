<template>
  <div class="settings-container">
    <h1>个人设置</h1>
    
    <el-card class="settings-card">
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="当前密码" prop="currentPassword">
          <el-input v-model="userForm.currentPassword" type="password" placeholder="请输入当前密码" />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="userForm.newPassword" type="password" placeholder="请输入新密码" />
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input v-model="userForm.confirmPassword" type="password" placeholder="请确认新密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm">保存修改</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElForm } from 'element-plus'
import { updateUser } from '@/api'

const userFormRef = ref(null)

const userForm = reactive({
  id: null,
  username: '',
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== userForm.newPassword) {
    callback(new Error('两次输入密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { min: 6, max: 20, message: '新密码长度在 6 到 20 个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const loadUserInfo = () => {
  try {
    console.log('开始加载用户信息...')
    const userInfoStr = localStorage.getItem('userInfo')
    console.log('localStorage中的userInfo:', userInfoStr)
    if (userInfoStr) {
      const userInfo = JSON.parse(userInfoStr)
      console.log('解析后的用户信息:', userInfo)
      userForm.id = userInfo.id
      userForm.username = userInfo.username
      console.log('更新后的userForm:', userForm)
    } else {
      console.log('localStorage中没有用户信息')
    }
  } catch (error) {
    console.error('加载用户信息失败:', error)
    ElMessage.error('加载用户信息失败')
  }
}

const submitForm = async () => {
  if (!userFormRef.value) return
  
  await userFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const updateData = {
          id: userForm.id,
          username: userForm.username,
          password: userForm.newPassword
        }
        
        const response = await updateUser(updateData)
        
        if (response.code === 200) {
          // 更新localStorage中的用户信息
          const userInfo = JSON.parse(localStorage.getItem('userInfo'))
          userInfo.username = userForm.username
          localStorage.setItem('userInfo', JSON.stringify(userInfo))
          
          ElMessage.success('修改成功')
          resetForm()
        } else {
          ElMessage.error(response.message || '修改失败')
        }
      } catch (error) {
        console.error('修改失败:', error)
        ElMessage.error('修改失败')
      }
    }
  })
}

const resetForm = () => {
  if (userFormRef.value) {
    userFormRef.value.resetFields()
    // 重新加载用户信息
    loadUserInfo()
  }
}

onMounted(() => {
  loadUserInfo()
})
</script>

<style scoped>
.settings-container {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.settings-card {
  max-width: 600px;
  margin: 0 auto;
}

:deep(.el-form-item) {
  margin-bottom: 20px;
}
</style>