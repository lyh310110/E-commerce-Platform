<template>
  <div class="product-detail-container">
    <el-card class="product-detail-card">
      <div class="product-header">
        <div class="product-images">
          <el-image
            :src="product.image || 'https://via.placeholder.com/400x400?text=No+Image'"
            fit="contain"
            class="main-image"
          />
        </div>
        <div class="product-info">
          <h1 class="product-title">{{ product.name }}</h1>
          <div class="product-price">¥{{ product.price }}</div>
          <div class="product-sales">销量: {{ product.sales || 0 }}</div>
          <div class="product-description">{{ product.description }}</div>
          <div class="product-shop">店铺: {{ product.shopName }}</div>
          <div class="product-actions">
            <el-button size="large" type="primary" @click="addProductToCart(product.id)">
              加入购物车
            </el-button>
            <el-button size="large" @click="toggleFavorite(product.id)">
              <el-icon><StarFilled v-if="isFavorited" /><Star v-else /></el-icon>
              收藏
            </el-button>
            <el-button size="large" @click="viewShop(product.shopId)">
              查看店铺
            </el-button>
          </div>
        </div>
      </div>
      
      <!-- 商品详情标签页 -->
      <el-tabs v-model="activeTab" type="border-card" style="margin-top: 20px;">
        <el-tab-pane label="商品详情" name="detail">
          <div class="product-detail-content">
            {{ product.detailDescription || product.description }}
          </div>
        </el-tab-pane>
        <el-tab-pane label="商品评论" name="comments">
          <div class="product-comments">
            <el-empty v-if="comments.length === 0" description="暂无评论" />
            <el-timeline v-else>
              <el-timeline-item
                v-for="comment in comments"
                :key="comment.id"
                :timestamp="formatDate(comment.createTime)"
              >
                <el-card>
                  <h4>{{ comment.userName }}</h4>
                  <p>{{ comment.content }}</p>
                  <div class="comment-rating">
                    <el-rate v-model="comment.rating" disabled show-score />
                  </div>
                </el-card>
              </el-timeline-item>
            </el-timeline>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { getProductById, addToCart as apiAddToCart, addFavorite, checkFavorite, removeFavorite, addFootprint, getProductComments } from '@/api'

const route = useRoute()
const router = useRouter()
const productId = ref(route.params.id)
const product = ref({})
const comments = ref([])
const activeTab = ref('detail')
const isFavorited = ref(false)

// 加载商品详情
const loadProductDetail = async () => {
  try {
    const response = await getProductById(productId.value)
    if (response.code === 200) {
      product.value = response.data
      // 记录足迹
      const userInfo = JSON.parse(localStorage.getItem('userInfo'))
      if (userInfo) {
        await addFootprint({
          userId: userInfo.id,
          productId: productId.value
        })
      }
      // 检查是否已收藏
      checkProductFavorite()
      // 加载评论
      loadComments()
    }
  } catch (error) {
    console.error('加载商品详情失败:', error)
    ElMessage.error('加载商品详情失败')
  }
}

// 检查商品是否已收藏
const checkProductFavorite = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (userInfo) {
      const response = await checkFavorite(userInfo.id, productId.value)
      if (response.code === 200) {
        isFavorited.value = response.data
      }
    }
  } catch (error) {
    console.error('检查收藏状态失败:', error)
  }
}

// 切换收藏状态
const toggleFavorite = async () => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    if (isFavorited.value) {
      // 取消收藏
      const response = await removeFavorite(null, userInfo.id, productId.value)
      if (response.code === 200) {
        isFavorited.value = false
        ElMessage.success('取消收藏成功')
      }
    } else {
      // 添加收藏
      const response = await addFavorite({
        userId: userInfo.id,
        productId: productId.value
      })
      if (response.code === 200) {
        isFavorited.value = true
        ElMessage.success('收藏成功')
      }
    }
  } catch (error) {
    console.error('操作收藏失败:', error)
    ElMessage.error('操作失败')
  }
}

// 添加到购物车
const addProductToCart = async (productId) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    const response = await apiAddToCart({
      userId: userInfo.id,
      productId: productId,
      quantity: 1
    })
    
    if (response.code === 200) {
      ElMessage.success('加入购物车成功')
    }
  } catch (error) {
    console.error('加入购物车失败:', error)
    ElMessage.error('加入购物车失败')
  }
}

// 查看店铺
const viewShop = (shopId) => {
  // 跳转到店铺页面（需要实现）
  ElMessage.info('店铺页面功能正在开发中')
}

// 加载评论
const loadComments = async () => {
  try {
    const response = await getProductComments(productId.value)
    if (response.code === 200) {
      comments.value = response.data
    }
  } catch (error) {
    console.error('加载评论失败:', error)
    // 评论功能可能尚未实现，这里不显示错误信息
  }
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleString('zh-CN')
}

onMounted(() => {
  loadProductDetail()
})
</script>

<style scoped>
.product-detail-container {
  padding: 20px;
}

.product-detail-card {
  padding: 20px;
}

.product-header {
  display: flex;
  flex-direction: row;
  gap: 30px;
}

.product-images {
  flex: 1;
  max-width: 500px;
}

.main-image {
  width: 100%;
  height: 400px;
  object-fit: contain;
}

.product-info {
  flex: 2;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.product-title {
  font-size: 24px;
  font-weight: bold;
  margin: 0;
}

.product-price {
  font-size: 28px;
  color: #f56c6c;
  font-weight: bold;
}

.product-sales,
.product-shop {
  font-size: 14px;
  color: #909399;
}

.product-description {
  font-size: 16px;
  color: #606266;
  line-height: 1.5;
}

.product-actions {
  display: flex;
  gap: 10px;
  margin-top: 20px;
}

.product-detail-content {
  padding: 20px;
  font-size: 16px;
  line-height: 1.8;
}

.product-comments {
  padding: 20px;
}

.comment-rating {
  margin-top: 10px;
}
</style>