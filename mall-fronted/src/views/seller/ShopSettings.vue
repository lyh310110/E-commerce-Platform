<template>
  <div class="shop-settings-container">
    <h2>基础店铺设置</h2>
    <el-form :model="shopInfo" :rules="rules" ref="shopForm" label-width="120px" class="shop-form">
      <el-form-item label="商铺名称" prop="shopName" required>
        <el-input v-model="shopInfo.shopName" placeholder="请输入商铺名称" />
      </el-form-item>
      <el-form-item label="商铺描述" prop="shopDesc">
        <el-input 
          v-model="shopInfo.shopDesc" 
          type="textarea" 
          placeholder="请输入商铺描述" 
          :rows="4"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="contactPhone">
        <el-input v-model="shopInfo.contactPhone" placeholder="请输入联系电话" />
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="shopStatus" active-value="1" inactive-value="0" active-text="营业" inactive-text="停业" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleUpdateShop">保存修改</el-button>
        <el-button @click="resetForm">重置</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElForm, ElInput, ElButton, ElSwitch, ElMessage } from 'element-plus'
import { getShopInfo, updateShop } from '@/api'

const shopForm = ref(null)
const shopInfo = reactive({
  id: null,
  userId: null,
  shopName: '',
  shopLogo: '',
  shopDesc: '',
  contactPhone: '',
  status: 1, // 1: 营业, 0: 停业
  auditRemark: '',
  createTime: '',
  updateTime: ''
})

// 使用ref处理状态，因为el-switch需要与后端Integer类型转换
const shopStatus = ref(1)

// 表单验证规则
const rules = {
  shopName: [
    { required: true, message: '请输入商铺名称', trigger: 'blur' },
    { min: 2, max: 20, message: '商铺名称长度在 2 到 20 个字符', trigger: 'blur' }
  ],
  contactPhone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

onMounted(() => {
  fetchShopInfo()
})

const fetchShopInfo = async () => {
  try {
    console.log('开始获取商铺信息...')
    // 从localStorage获取用户信息
    const userInfo = localStorage.getItem('userInfo')
    console.log('localStorage中的userInfo:', userInfo)
    if (!userInfo) {
      console.log('localStorage中没有userInfo')
      return
    }
    
    const parsedUserInfo = JSON.parse(userInfo)
    const userId = parsedUserInfo.id || 6 // 默认使用用户ID=6
    console.log('使用的userId:', userId, '类型:', typeof userId)
    
    const response = await getShopInfo(userId)
    console.log('getShopInfo响应:', JSON.stringify(response))
    if (response.success) {
      // 确保id和userId是数字类型
      const shopData = response.data
      console.log('从后端获取的shopData:', JSON.stringify(shopData))
      console.log('shopData.id:', shopData.id, '类型:', typeof shopData.id)
      
      if (shopData.id) {
        shopData.id = Number(shopData.id)
        console.log('转换后的shopData.id:', shopData.id, '类型:', typeof shopData.id)
      }
      if (shopData.userId) {
        shopData.userId = Number(shopData.userId)
        console.log('转换后的shopData.userId:', shopData.userId, '类型:', typeof shopData.userId)
      }
      
      Object.assign(shopInfo, shopData)
      // 将后端的Integer状态转换为el-switch可用的状态
      shopStatus.value = shopData.status
      
      console.log('赋值后的shopInfo:', JSON.stringify(shopInfo))
      console.log('赋值后shopInfo.id:', shopInfo.id, '类型:', typeof shopInfo.id)
    } else {
      console.log('获取商铺信息失败:', response.msg || '未知错误')
    }
  } catch (error) {
    console.error('获取商铺信息失败（异常）:', error)
    console.error('错误详情:', error.stack)
  }
}

const handleUpdateShop = async () => {
  try {
    console.log('开始更新商铺信息...')
    // 表单验证
    if (!shopInfo.shopName.trim()) {
      console.log('商铺名称不能为空')
      return
    }
    
    // 将开关状态同步到shopInfo对象
    shopInfo.status = shopStatus.value
    console.log('shopInfo状态:', shopInfo)
    
    // 确保id和userId是数字类型，避免类型转换问题
    const updateData = {
      ...shopInfo,
      id: shopInfo.id !== null ? Number(shopInfo.id) : null,
      userId: shopInfo.userId !== null ? Number(shopInfo.userId) : null,
      status: Number(shopInfo.status)
    }
    
    console.log('准备发送给后端的数据:', updateData)
    console.log('updateData.id:', updateData.id, '类型:', typeof updateData.id)
    console.log('updateData.userId:', updateData.userId, '类型:', typeof updateData.userId)
    
    const response = await updateShop(updateData)
    console.log('updateShop响应:', JSON.stringify(response))
    if (response.success) {
      console.log('保存成功')
      ElMessage.success('店铺信息更新成功！')
    } else {
      console.log('保存失败:', response.msg)
      ElMessage.error(response.msg || '店铺信息更新失败！')
    }
  } catch (error) {
    console.error('更新商铺信息失败:', error)
    console.error('错误详情:', error.stack)
  }
}

const resetForm = () => {
  fetchShopInfo()
}
</script>

<style scoped>
.shop-settings-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.shop-form {
  margin-top: 20px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}
</style>