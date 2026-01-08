<template>
  <div class="my-page-container">
    <h1>我的页面</h1>
    
    <el-card class="my-page-card">
      <el-tabs v-model="activeTab" type="card" @tab-change="handleTabChange">
        <!-- 我的收藏 -->
        <el-tab-pane label="我的收藏" name="favorites">
          <div class="tab-content">
            <el-empty v-if="favorites.length === 0" description="暂无收藏商品" />
            <el-row :gutter="20" v-else>
              <el-col :span="6" v-for="item in favorites" :key="item.id">
                <el-card class="favorite-card" shadow="hover">
                  <template #header>
                    <div class="card-header">
                      <span>{{ item.product ? item.product.name : `商品${item.productId}` }}</span>
                      <el-button type="text" @click="handleRemoveFavorite(item.id)">
                        <el-icon><Delete /></el-icon>
                      </el-button>
                    </div>
                  </template>
                  <div class="card-content">
                    <p v-if="item.product">价格：¥{{ item.product.price }}</p>
                    <p>收藏时间：{{ formatDate(item.createTime) }}</p>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
        
        <!-- 我的足迹 -->
        <el-tab-pane label="我的足迹" name="footprints">
          <div class="tab-content">
            <div class="footprint-header">
              <h3>浏览历史</h3>
              <el-button type="primary" @click="handleClearFootprints" danger>
                清空足迹
              </el-button>
            </div>
            <el-empty v-if="footprints.length === 0" description="暂无浏览记录" />
            <el-timeline v-else>
              <el-timeline-item
                v-for="item in footprints"
                :key="item.id"
                :timestamp="formatDate(item.visitTime)"
              >
                {{ item.product ? item.product.name : `商品${item.productId}` }}
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-tab-pane>
        

        
        <!-- 积分系统 -->
        <el-tab-pane label="积分系统" name="points">
          <div class="tab-content">
            <el-card class="points-summary-card">
              <div class="points-summary">
                <div class="points-value">{{ userPoint.points || 0 }}</div>
                <div class="points-label">我的积分</div>
              </div>
            </el-card>
            
            <h3>积分记录</h3>
            <el-empty v-if="pointRecords.length === 0" description="暂无积分记录" />
            <el-table v-else :data="pointRecords" style="width: 100%" stripe>
              <el-table-column prop="points" label="积分" width="120">
                <template #default="scope">
                  <span :style="scope.row.type === 1 ? 'color: green' : 'color: red'">
                    {{ scope.row.type === 1 ? '+' : '-' }}{{ scope.row.points }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column prop="reason" label="原因" />
              <el-table-column prop="recordTime" label="记录时间" width="200">
                <template #default="scope">
                  {{ formatDate(scope.row.recordTime) }}
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
        
        <!-- 客服咨询 -->
        <el-tab-pane label="客服咨询" name="customer-service">
          <div class="tab-content">
            <el-button type="primary" @click="showAddConsultationDialog = true">
              <el-icon><Plus /></el-icon> 提交咨询
            </el-button>
            
            <h3 style="margin-top: 30px;">我的咨询</h3>
            <el-empty v-if="consultations.length === 0" description="暂无咨询记录" />
            <el-card class="consultation-card" shadow="hover" v-for="item in consultations" :key="item.id">
              <div class="consultation-header">
                <div class="consultation-title">{{ item.title }}</div>
                <el-tag :type="item.status === 0 ? 'warning' : 'success'">
                  {{ item.status === 0 ? '未回复' : '已回复' }}
                </el-tag>
              </div>
              <div class="consultation-content">
                {{ item.content }}
              </div>
              <div class="consultation-footer">
                <span>{{ formatDate(item.createTime) }}</span>
                <el-button type="text" @click="handleViewConsultation(item.id)">
                  查看详情
                </el-button>
              </div>
            </el-card>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    
    <!-- 提交咨询对话框 -->
    <el-dialog
      v-model="showAddConsultationDialog"
      title="提交咨询"
      width="500px"
    >
      <el-form :model="consultationForm" :rules="consultationRules" ref="consultationFormRef" label-width="100px">
        <el-form-item label="咨询标题" prop="title">
          <el-input v-model="consultationForm.title" placeholder="请输入咨询标题" />
        </el-form-item>
        <el-form-item label="咨询内容" prop="content">
          <el-input v-model="consultationForm.content" type="textarea" placeholder="请输入咨询内容" :rows="5" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showAddConsultationDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmitConsultation">确认提交</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 咨询详情对话框 -->
    <el-dialog
      v-model="showConsultationDetailDialog"
      :title="selectedConsultation.title"
      width="600px"
    >
      <div class="consultation-detail">
        <div class="detail-item">
          <span class="detail-label">咨询内容：</span>
          <span>{{ selectedConsultation.content }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">提交时间：</span>
          <span>{{ formatDate(selectedConsultation.createTime) }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">状态：</span>
          <el-tag :type="selectedConsultation.status === 0 ? 'warning' : 'success'">
            {{ selectedConsultation.status === 0 ? '未回复' : '已回复' }}
          </el-tag>
        </div>
        
        <h4>回复内容</h4>
        <el-empty v-if="consultationReplies.length === 0" description="暂无回复" />
        <div class="reply-item" v-for="item in consultationReplies" :key="item.id">
          <div class="reply-content">{{ item.content }}</div>
          <div class="reply-time">{{ formatDate(item.replyTime) }}</div>
        </div>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="showConsultationDetailDialog = false">关闭</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElForm, ElEmpty, ElRow, ElCol, ElCard, ElTabs, ElTabPane, ElTimeline, ElTimelineItem, ElTag, ElButton, ElIcon, ElDialog, ElInput, ElFormItem } from 'element-plus'
import { Delete, Plus } from '@element-plus/icons-vue'
import {
  getFavorites, removeFavorite,
  getFootprints, clearFootprints,
  getUserPoint, getPointRecords,
  getConsultations, getConsultationDetail, getConsultationReplies, addConsultation,
  getProductById
} from '@/api'

const activeTab = ref('favorites')
const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
const userId = userInfo.id || 1 // 设置默认userId为1，用于测试

// 收藏相关数据
const favorites = ref([])

// 足迹相关数据
const footprints = ref([])



// 积分相关数据
const userPoint = ref({ points: 0 })
const pointRecords = ref([])

// 客服咨询相关数据
const consultations = ref([])
const selectedConsultation = ref({})
const consultationReplies = ref([])
const showAddConsultationDialog = ref(false)
const showConsultationDetailDialog = ref(false)
const consultationFormRef = ref(null)
const consultationForm = reactive({
  title: '',
  content: ''
})
const consultationRules = {
  title: [
    { required: true, message: '请输入咨询标题', trigger: 'blur' },
    { min: 5, max: 50, message: '标题长度在 5 到 50 个字符', trigger: 'blur' }
  ],
  content: [
    { required: true, message: '请输入咨询内容', trigger: 'blur' },
    { min: 10, max: 500, message: '内容长度在 10 到 500 个字符', trigger: 'blur' }
  ]
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

// 加载收藏列表
const loadFavorites = async () => {
  try {
    const response = await getFavorites(userId)
    if (response.code === 200) {
      const favoriteList = response.data || []
      
      // 获取每个收藏对应的商品详情
      const favoritesWithProduct = await Promise.all(favoriteList.map(async (favorite) => {
        try {
          const productResponse = await getProductById(favorite.productId)
          if (productResponse.code === 200) {
            return {
              ...favorite,
              product: productResponse.data
            }
          }
          return favorite
        } catch (error) {
          console.error(`获取商品${favorite.productId}详情失败:`, error)
          return favorite
        }
      }))
      
      favorites.value = favoritesWithProduct
    }
  } catch (error) {
    console.error('加载收藏列表失败:', error)
    ElMessage.error('加载收藏列表失败')
  }
}

// 删除收藏
const handleRemoveFavorite = async (id) => {
  try {
    const response = await removeFavorite(id)
    if (response.code === 200) {
      ElMessage.success('删除收藏成功')
      loadFavorites()
    }
  } catch (error) {
    console.error('删除收藏失败:', error)
    ElMessage.error('删除收藏失败')
  }
}

// 加载足迹列表
const loadFootprints = async () => {
  try {
    const response = await getFootprints(userId)
    if (response.code === 200) {
      const footprintList = response.data || []
      
      // 获取每个足迹对应的商品详情
      const footprintsWithProduct = await Promise.all(footprintList.map(async (footprint) => {
        try {
          const productResponse = await getProductById(footprint.productId)
          if (productResponse.code === 200) {
            return {
              ...footprint,
              product: productResponse.data
            }
          }
          return footprint
        } catch (error) {
          console.error(`获取商品${footprint.productId}详情失败:`, error)
          return footprint
        }
      }))
      
      footprints.value = footprintsWithProduct
    }
  } catch (error) {
    console.error('加载足迹列表失败:', error)
    ElMessage.error('加载足迹列表失败')
  }
}

// 清空足迹
const handleClearFootprints = async () => {
  try {
    const response = await clearFootprints(userId)
    if (response.code === 200) {
      ElMessage.success('清空足迹成功')
      footprints.value = []
    }
  } catch (error) {
    console.error('清空足迹失败:', error)
    ElMessage.error('清空足迹失败')
  }
}



// 加载积分信息
const loadPointInfo = async () => {
  try {
    // 加载用户积分
    const pointResponse = await getUserPoint(userId)
    if (pointResponse.code === 200) {
      userPoint.value = pointResponse.data
    }
    
    // 加载积分记录
    const recordsResponse = await getPointRecords(userId)
    if (recordsResponse.code === 200) {
      pointRecords.value = recordsResponse.data
    }
  } catch (error) {
    console.error('加载积分信息失败:', error)
    ElMessage.error('加载积分信息失败')
  }
}

// 加载咨询列表
const loadConsultations = async () => {
  try {
    const response = await getConsultations(userId)
    if (response.code === 200) {
      consultations.value = response.data
    }
  } catch (error) {
    console.error('加载咨询列表失败:', error)
    ElMessage.error('加载咨询列表失败')
  }
}

// 查看咨询详情
const handleViewConsultation = async (id) => {
  try {
    const detailResponse = await getConsultationDetail(id)
    if (detailResponse.code === 200) {
      selectedConsultation.value = detailResponse.data
      
      const repliesResponse = await getConsultationReplies(id)
      if (repliesResponse.code === 200) {
        consultationReplies.value = repliesResponse.data
      }
      
      showConsultationDetailDialog.value = true
    }
  } catch (error) {
    console.error('加载咨询详情失败:', error)
    ElMessage.error('加载咨询详情失败')
  }
}

// 提交咨询
const handleSubmitConsultation = async () => {
  if (!consultationFormRef.value) return
  
  await consultationFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const consultationData = {
          ...consultationForm,
          userId: userId
        }
        
        const response = await addConsultation(consultationData)
        if (response.code === 200) {
          ElMessage.success('提交咨询成功')
          showAddConsultationDialog.value = false
          loadConsultations()
          // 重置表单
          consultationForm.title = ''
          consultationForm.content = ''
        }
      } catch (error) {
        console.error('提交咨询失败:', error)
        ElMessage.error('提交咨询失败')
      }
    }
  })
}

// 标签切换时加载对应数据
const handleTabChange = (tabName) => {
  switch (tabName) {
    case 'favorites':
      loadFavorites()
      break
    case 'footprints':
      loadFootprints()
      break

    case 'points':
      loadPointInfo()
      break
    case 'customer-service':
      loadConsultations()
      break
  }
}

onMounted(() => {
  handleTabChange('favorites') // 初始加载收藏数据
})
</script>

<style scoped>
.my-page-container {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.my-page-card {
  min-height: 600px;
}

.tab-content {
  padding: 20px 0;
}

/* 收藏样式 */
.favorite-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {
  padding: 10px 0;
}

/* 足迹样式 */
.footprint-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}



/* 积分样式 */
.points-summary-card {
  margin-bottom: 20px;
}

.points-summary {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 0;
}

.points-value {
  font-size: 48px;
  font-weight: bold;
  color: #e6a23c;
}

.points-label {
  font-size: 18px;
  color: #606266;
  margin-top: 10px;
}

/* 客服咨询样式 */
.consultation-card {
  margin-bottom: 20px;
}

.consultation-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.consultation-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.consultation-content {
  margin-bottom: 15px;
  color: #606266;
  line-height: 1.6;
}

.consultation-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #909399;
  font-size: 14px;
}

.consultation-detail {
  padding: 20px 0;
}

.detail-item {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
}

.detail-label {
  font-weight: bold;
  margin-right: 10px;
  width: 100px;
}

.reply-item {
  margin-bottom: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 5px;
}

.reply-content {
  margin-bottom: 10px;
  color: #303133;
}

.reply-time {
  font-size: 12px;
  color: #909399;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
