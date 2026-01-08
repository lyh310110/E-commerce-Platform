<template>
  <div class="footprints-container">
    <h1>我的足迹</h1>
    
    <!-- 操作栏 -->
    <div class="actions-bar">
      <el-button type="danger" @click="handleClearFootprints">
        <el-icon><Delete /></el-icon> 清空足迹
      </el-button>
    </div>

    <!-- 足迹列表 -->
    <div class="footprints-grid">
      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="footprint in footprints" :key="footprint.id">
          <el-card :body-style="{ padding: '0px' }" class="product-card">
            <img :src="footprint.product.image || 'https://via.placeholder.com/200x200?text=No+Image'" class="product-image" @click="viewDetail(footprint.product.id)" />
            <div style="padding: 14px;">
              <h3 class="product-title" @click="viewDetail(footprint.product.id)">{{ footprint.product.name }}</h3>
              <div class="product-price">¥{{ footprint.product.price }}</div>
              <div class="product-shop">店铺: {{ footprint.product.shopName }}</div>
              <div class="product-visit-time">访问时间: {{ formatVisitTime(footprint.visitTime) }}</div>
              <div class="product-actions">
                <el-button size="small" type="primary" @click="addProductToCart(footprint.product.id)">加入购物车</el-button>
                <el-button size="small" @click="toggleFavorite(footprint.product.id)">
                  <el-icon><StarFilled v-if="favoritedProducts[footprint.product.id]" /><Star v-else /></el-icon>
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && footprints.length === 0" class="empty-state">
      <el-empty description="暂无浏览记录" />
    </div>

    <!-- 分页 -->
    <div class="pagination" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[12, 24, 36]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Star, StarFilled, Delete } from '@element-plus/icons-vue'
import { getFootprints, addToCart as apiAddToCart, addFavorite, checkFavorite, removeFavorite, getProductById, clearFootprints } from '@/api'

const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const footprints = ref([])
const favoritedProducts = ref({}) // 跟踪商品的收藏状态，键为商品ID，值为布尔值

// 格式化访问时间
const formatVisitTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  return date.toLocaleString('zh-CN')
}

// 查看商品详情
const viewDetail = (productId) => {
  // 跳转到商品详情页
  window.location.href = `/buyer/products/${productId}`
}

// 添加到购物车
const addProductToCart = async (productId) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    // 查找商品信息
    const footprint = footprints.value.find(f => f.product.id === productId)
    if (!footprint || !footprint.product) {
      ElMessage.error('商品信息不存在')
      return
    }
    
    // 调用API加入购物车
    const response = await apiAddToCart({
      userId: userInfo.id,
      productId: productId,
      productName: footprint.product.name,
      price: footprint.product.price,
      num: 1
    })
    
    if (response.code === 200) {
      ElMessage.success('加入购物车成功')
    } else {
      ElMessage.error('加入购物车失败')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    ElMessage.error('加入购物车失败')
  }
}

// 切换收藏状态
const toggleFavorite = async (productId) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    // 检查当前收藏状态
    const checkResponse = await checkFavorite(userInfo.id, productId)
    const favoriteItem = checkResponse.data
    const isFavorited = favoriteItem !== null
    
    if (isFavorited) {
      // 已经收藏，调用取消收藏API
      const response = await removeFavorite(favoriteItem.id)
      if (response.code === 200) {
        favoritedProducts.value[productId] = false
        ElMessage.success('取消收藏成功')
      } else {
        ElMessage.error('取消收藏失败')
      }
    } else {
      // 未收藏，调用添加收藏API
      const favoriteData = {
        userId: userInfo.id,
        productId: productId
      }
      const response = await addFavorite(favoriteData)
      if (response.code === 200) {
        favoritedProducts.value[productId] = true
        ElMessage.success('收藏成功')
      } else {
        ElMessage.error('收藏失败')
      }
    }
  } catch (error) {
    console.error('切换收藏状态失败:', error)
    ElMessage.error('操作失败，请重试')
  }
}

// 加载足迹数据
const loadFootprints = async () => {
  loading.value = true
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      loading.value = false
      return
    }

    const response = await getFootprints(userInfo.id)
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
      
      // 按访问时间降序排序
      footprints.value = footprintsWithProduct.sort((a, b) => {
        return new Date(b.visitTime) - new Date(a.visitTime)
      })
      
      // 初始化收藏状态
      await Promise.all(footprints.value.map(async (footprint) => {
        if (footprint.product) {
          try {
            const checkResponse = await checkFavorite(userInfo.id, footprint.product.id)
            favoritedProducts.value[footprint.product.id] = checkResponse.data !== null
          } catch (error) {
            console.error(`检查商品${footprint.product.id}收藏状态失败:`, error)
          }
        }
      }))
      
      total.value = footprints.value.length
    }
  } catch (error) {
    console.error('获取足迹列表失败:', error)
    ElMessage.error('获取足迹列表失败')
    footprints.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

// 清空足迹
const handleClearFootprints = async () => {
  try {
    console.log('开始清空足迹...')
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    console.log('用户信息:', userInfo)
    if (!userInfo) {
      console.log('用户未登录，不执行清空操作')
      ElMessage.warning('请先登录')
      return
    }
    
    // 确认清空操作
    console.log('确认清空操作...')
    await ElMessageBox.confirm('确定要清空所有足迹记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 调用清空API
    console.log('调用清空足迹API，用户ID:', userInfo.id)
    const response = await clearFootprints(userInfo.id)
    console.log('清空足迹API响应:', response)
    if (response.code === 200) {
      console.log('清空足迹成功')
      footprints.value = []
      total.value = 0
      ElMessage.success('清空足迹成功')
    } else {
      console.log('清空足迹失败，API返回错误:', response.msg)
      ElMessage.error('清空足迹失败')
    }
  } catch (error) {
    if (error === 'cancel') {
      console.log('用户取消清空操作')
    } else {
      console.error('清空足迹失败:', error)
      ElMessage.error('清空足迹失败')
    }
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadFootprints()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadFootprints()
}

// 页面加载时初始化数据
onMounted(async () => {
  await loadFootprints()
})
</script>

<style scoped>
.footprints-container {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

.actions-bar {
  margin-bottom: 30px;
  text-align: right;
}

.footprints-grid {
  margin-bottom: 30px;
}

.product-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-title {
  font-size: 16px;
  font-weight: bold;
  color: #333;
  margin: 10px 0;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-title:hover {
  color: #1890ff;
}

.product-price {
  font-size: 18px;
  font-weight: bold;
  color: #ff4757;
  margin-bottom: 5px;
}

.product-shop {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.product-visit-time {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
}

.product-actions {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.empty-state {
  margin: 50px 0;
}

.pagination {
  text-align: center;
  margin-top: 30px;
}
</style>