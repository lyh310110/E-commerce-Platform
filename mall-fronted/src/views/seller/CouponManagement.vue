<template>
  <div class="coupon-management-container">
    <h2 class="page-title">优惠券管理</h2>
    
    <!-- 创建优惠券按钮 -->
    <div class="create-coupon-btn">
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        创建优惠券
      </el-button>
    </div>
    
    <!-- 优惠券列表 -->
    <div class="coupons-list">
      <el-table :data="coupons" style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="优惠券名称" />
        <el-table-column prop="description" label="描述" width="200" />
        <el-table-column label="类型" width="100">
          <template #default="scope">
            {{ scope.row.type === 1 ? '满减券' : '折扣券' }}
          </template>
        </el-table-column>
        <el-table-column label="金额" width="100">
          <template #default="scope">
            ¥{{ scope.row.value }}
          </template>
        </el-table-column>
        <el-table-column label="使用条件" width="120">
          <template #default="scope">
            满¥{{ scope.row.minAmount }}可用
          </template>
        </el-table-column>
        <el-table-column label="有效期" width="250">
          <template #default="scope">
            {{ formatDate(scope.row.startTime) }} 至 {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column label="库存" width="150">
          <template #default="scope">
            {{ scope.row.remainingQuantity }}/{{ scope.row.totalQuantity }}
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button size="small" @click="editCoupon(scope.row)">
              编辑
            </el-button>
            <el-button size="small" type="danger" @click="handleDeleteCoupon(scope.row.id)">
              删除
            </el-button>
            <el-button size="small" type="primary" @click="handleToggleCouponStatus(scope.row)">
              {{ scope.row.status === 1 ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 创建/编辑优惠券对话框 -->
    <el-dialog
      v-model="showCreateDialog"
      title="{{ editingCoupon.id ? '编辑优惠券' : '创建优惠券' }}"
      width="500px"
    >
      <el-form :model="editingCoupon" label-width="100px">
        <el-form-item label="优惠券名称">
          <el-input v-model="editingCoupon.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input type="textarea" v-model="editingCoupon.description" placeholder="请输入优惠券描述" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="editingCoupon.type" placeholder="请选择优惠券类型">
            <el-option label="满减券" :value="1" />
            <el-option label="折扣券" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额">
          <el-input-number
            v-model="editingCoupon.value"
            :min="0.01"
            :max="1000"
            :step="0.01"
            placeholder="请输入优惠金额"
          />
        </el-form-item>
        <el-form-item label="使用条件">
          <el-input-number
            v-model="editingCoupon.minAmount"
            :min="0"
            :max="10000"
            :step="0.01"
            placeholder="请输入最低消费金额"
          />
        </el-form-item>
        <el-form-item label="开始时间">
          <el-date-picker
            v-model="editingCoupon.startTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="结束时间">
          <el-date-picker
            v-model="editingCoupon.endTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="总数量">
          <el-input-number
            v-model="editingCoupon.totalQuantity"
            :min="1"
            :max="10000"
            placeholder="请输入优惠券总数量"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-switch v-model="editingCoupon.status" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showCreateDialog = false">取消</el-button>
          <el-button type="primary" @click="saveCoupon">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getSellerCoupons, createCoupon, updateCoupon, deleteCoupon, toggleCouponStatus } from '@/api'

const coupons = ref([])
const showCreateDialog = ref(false)
const editingCoupon = ref({
  id: null,
  name: '',
  description: '',
  type: 1,
  value: 0,
  minAmount: 0,
  startTime: new Date(),
  endTime: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
  totalQuantity: 100,
  remainingQuantity: 100,
  status: 1
})

// 加载优惠券列表
const loadCoupons = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    console.log('用户信息:', userInfo)
    console.log('店铺ID:', userInfo.shopId)
    
    const response = await getSellerCoupons(userInfo.shopId)
    console.log('优惠券列表响应:', response)
    if (response.code === 200) {
      coupons.value = response.data
      console.log('优惠券列表:', coupons.value)
    }
  } catch (error) {
    console.error('加载优惠券列表失败:', error)
    ElMessage.error('加载优惠券列表失败')
  }
}

// 编辑优惠券
const editCoupon = (coupon) => {
  editingCoupon.value = { ...coupon }
  showCreateDialog.value = true
}

// 保存优惠券
const saveCoupon = async () => {
  try {
    // 验证表单
    if (!editingCoupon.value.name) {
      ElMessage.warning('请输入优惠券名称')
      return
    }
    
    if (editingCoupon.value.startTime >= editingCoupon.value.endTime) {
      ElMessage.warning('开始时间必须早于结束时间')
      return
    }
    
    let response
    if (editingCoupon.value.id) {
      // 更新优惠券
      response = await updateCoupon(editingCoupon.value)
    } else {
      // 创建优惠券
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (!userInfo) {
        ElMessage.warning('请先登录')
        return
      }
      
      const newCoupon = {
        ...editingCoupon.value,
        shopId: userInfo.shopId
      }
      
      response = await createCoupon(newCoupon)
    }
    
    if (response.code === 200) {
      ElMessage.success(editingCoupon.value.id ? '更新优惠券成功' : '创建优惠券成功')
      showCreateDialog.value = false
      // 重置表单
      resetCouponForm()
      // 重新加载数据
      loadCoupons()
    }
  } catch (error) {
    console.error('保存优惠券失败:', error)
    ElMessage.error(editingCoupon.value.id ? '更新优惠券失败' : '创建优惠券失败')
  }
}

// 删除优惠券
const handleDeleteCoupon = async (id) => {
  try {
    const response = await deleteCoupon(id)
    if (response.code === 200) {
      ElMessage.success('删除优惠券成功')
      // 重新加载数据
      loadCoupons()
    }
  } catch (error) {
    console.error('删除优惠券失败:', error)
    ElMessage.error('删除优惠券失败')
  }
}

// 切换优惠券状态
const handleToggleCouponStatus = async (coupon) => {
  try {
    const newStatus = coupon.status === 1 ? 0 : 1
    const response = await toggleCouponStatus(coupon.id, newStatus)
    if (response.code === 200) {
      ElMessage.success(`优惠券已${newStatus === 1 ? '启用' : '禁用'}`)
      // 重新加载数据
      loadCoupons()
    }
  } catch (error) {
    console.error('切换优惠券状态失败:', error)
    ElMessage.error('切换优惠券状态失败')
  }
}

// 重置表单
const resetCouponForm = () => {
  editingCoupon.value = {
    id: null,
    name: '',
    description: '',
    type: 1,
    value: 0,
    minAmount: 0,
    startTime: new Date(),
    endTime: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000),
    totalQuantity: 100,
    remainingQuantity: 100,
    status: 1
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 初始化数据
onMounted(() => {
  loadCoupons()
})
</script>

<style scoped>
.coupon-management-container {
  padding: 20px;
}

.page-title {
  font-size: 24px;
  margin-bottom: 20px;
  color: #333;
}

.create-coupon-btn {
  margin-bottom: 20px;
}

.coupons-list {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}
</style>