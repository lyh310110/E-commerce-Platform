<template>
  <div class="home-container">
    <h1>欢迎使用商城管理系统</h1>
    
    <!-- 统计数据卡片 -->
    <div class="statistics-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon user-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ statistics.totalUsers || 0 }}</div>
                <div class="stat-label">总用户数</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon buyer-icon">
                <el-icon><ShoppingCart /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ statistics.buyerCount || 0 }}</div>
                <div class="stat-label">买家数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon seller-icon">
                <el-icon><Shop /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ statistics.sellerCount || 0 }}</div>
                <div class="stat-label">卖家数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
        
        <el-col :span="6">
          <el-card class="stat-card" shadow="hover">
            <div class="stat-content">
              <div class="stat-icon shop-icon">
                <el-icon><OfficeBuilding /></el-icon>
              </div>
              <div class="stat-info">
                <div class="stat-value">{{ statistics.shopCount || 0 }}</div>
                <div class="stat-label">店铺数量</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { User, ShoppingCart, Shop, OfficeBuilding } from '@element-plus/icons-vue'
import { getDashboardStatistics } from '@/api'

const statistics = ref({})

const loadStatistics = async () => {
  try {
    const response = await getDashboardStatistics()
    if (response.code === 200) {
      statistics.value = response.data
    } else {
      ElMessage.error('获取统计数据失败')
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    ElMessage.error('获取统计数据失败')
  }
}

onMounted(() => {
  loadStatistics()
})
</script>

<style scoped>
.home-container {
  padding: 20px;
}

h1 {
  font-size: 32px;
  color: #333;
  text-align: center;
  margin-bottom: 30px;
}

.statistics-cards {
  margin-bottom: 30px;
}

.stat-card {
  height: 120px;
  border-radius: 8px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  font-size: 24px;
  color: white;
}

.user-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.buyer-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.seller-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.shop-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}



.card-header {
  font-weight: bold;
  color: #303133;
}
</style>
