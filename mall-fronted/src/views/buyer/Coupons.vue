<template>
  <div class="coupons-container">
    <h2 class="page-title">我的优惠券</h2>
    
    <!-- 优惠券标签页 -->
    <el-tabs v-model="activeTab" type="border-card" style="margin-bottom: 20px;">
      <el-tab-pane label="可领取优惠券" name="available">
        <div class="coupons-list">
          <el-empty v-if="availableCoupons.length === 0" description="暂无可领取优惠券" />
          <div class="coupon-card" v-for="coupon in availableCoupons" :key="coupon.id">
            <div class="coupon-info">
              <div class="coupon-value">¥{{ coupon.value }}</div>
              <div class="coupon-condition">满{{ coupon.minAmount }}元可用</div>
              <div class="coupon-name">{{ coupon.name }}</div>
              <div class="coupon-period">有效期：{{ formatDate(coupon.startTime) }}至{{ formatDate(coupon.endTime) }}</div>
            </div>
            <div class="coupon-action">
              <el-button type="primary" size="small" @click="claimCoupon(coupon.id)">
                立即领取
              </el-button>
            </div>
          </div>
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="我的优惠券" name="my">
        <div class="filter-tabs">
          <el-tabs v-model="couponFilter" type="card">
            <el-tab-pane label="未使用" name="1" />
            <el-tab-pane label="已使用" name="2" />
            <el-tab-pane label="已过期" name="3" />
          </el-tabs>
        </div>
        
        <div class="coupons-list">
          <el-empty v-if="filteredCoupons.length === 0" description="暂无优惠券" />
          <div class="coupon-card" v-for="userCoupon in filteredCoupons" :key="userCoupon.id">
            <div class="coupon-info">
              <div class="coupon-value">¥{{ userCoupon.coupon.value }}</div>
              <div class="coupon-condition">满{{ userCoupon.coupon.minAmount }}元可用</div>
              <div class="coupon-name">{{ userCoupon.coupon.name }}</div>
              <div class="coupon-period">有效期：{{ formatDate(userCoupon.coupon.startTime) }}至{{ formatDate(userCoupon.coupon.endTime) }}</div>
              <div class="coupon-obtain">领取时间：{{ formatDate(userCoupon.obtainTime) }}</div>
              <div class="coupon-status" :class="getStatusClass(userCoupon.status)">
                {{ getStatusText(userCoupon.status) }}
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getAvailableCoupons, getUserCoupons, obtainCoupon } from '@/api'

const activeTab = ref('available')
const couponFilter = ref('1')
const availableCoupons = ref([])
const userCoupons = ref([])

// 加载可领取优惠券
const loadAvailableCoupons = async () => {
  try {
    const response = await getAvailableCoupons()
    if (response.code === 200) {
      availableCoupons.value = response.data
    }
  } catch (error) {
    console.error('加载可领取优惠券失败:', error)
    ElMessage.error('加载可领取优惠券失败')
  }
}

// 加载用户优惠券
const loadUserCoupons = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    const response = await getUserCoupons(userInfo.id)
    if (response.code === 200) {
      userCoupons.value = response.data
    }
  } catch (error) {
    console.error('加载用户优惠券失败:', error)
    ElMessage.error('加载用户优惠券失败')
  }
}

// 领取优惠券
const claimCoupon = async (couponId) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    const response = await obtainCoupon(userInfo.id, couponId)
    if (response.code === 200) {
      ElMessage.success('领取优惠券成功')
      // 重新加载数据
      loadAvailableCoupons()
      loadUserCoupons()
    }
  } catch (error) {
    console.error('领取优惠券失败:', error)
    ElMessage.error('领取优惠券失败')
  }
}

// 过滤优惠券
const filteredCoupons = computed(() => {
  return userCoupons.value.filter(coupon => coupon.status === parseInt(couponFilter.value))
})

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN')
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    1: '未使用',
    2: '已使用',
    3: '已过期'
  }
  return statusMap[status] || '未知状态'
}

// 获取状态样式类
const getStatusClass = (status) => {
  const classMap = {
    1: 'status-unused',
    2: 'status-used',
    3: 'status-expired'
  }
  return classMap[status] || ''
}

// 初始化数据
onMounted(() => {
  loadAvailableCoupons()
  loadUserCoupons()
})
</script>

<style scoped>
.coupons-container {
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.coupons-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  padding: 20px 0;
}

.coupon-card {
  width: 320px;
  background: #fff;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 15px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.coupon-info {
  flex: 1;
}

.coupon-value {
  font-size: 32px;
  font-weight: bold;
  color: #f56c6c;
  margin-bottom: 5px;
}

.coupon-condition {
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
}

.coupon-name {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

.coupon-period {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.coupon-obtain {
  font-size: 12px;
  color: #909399;
  margin-bottom: 10px;
}

.coupon-action {
  position: absolute;
  top: 20px;
  right: 20px;
}

.coupon-status {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 10px;
  font-size: 12px;
  color: #fff;
}

.status-unused {
  background-color: #67c23a;
}

.status-used {
  background-color: #909399;
}

.status-expired {
  background-color: #e6a23c;
}

.filter-tabs {
  margin-bottom: 20px;
}
</style>