<template>
  <div class="products-container">
    <h1>商品浏览</h1>
    
    <!-- 店铺导航栏 -->
    <div class="shop-nav">
      <el-menu
        :default-active="activeShop"
        class="shop-menu"
        @select="handleShopSelect"
      >
        <el-menu-item index="all">
          <el-icon><Shop /></el-icon>
          <span>全部店铺</span>
        </el-menu-item>
        <el-menu-item 
                  v-for="shop in shops" 
                  :key="shop.id" 
                  :index="shop.id.toString()"
                >
                  <el-icon><Shop /></el-icon>
                  <span>{{ shop.shopName || shop.name }}</span>
                </el-menu-item>
      </el-menu>
    </div>

    <!-- 搜索和筛选区域 -->
    <div class="search-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索商品名称"
            @keyup.enter="handleSearch"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-select v-model="sortBy" placeholder="排序方式" @change="handleSearch">
            <el-option label="价格从低到高" value="price_asc" />
            <el-option label="价格从高到低" value="price_desc" />
            <el-option label="销量优先" value="sales_desc" />
            <el-option label="最新上架" value="newest" />
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-col>
      </el-row>
    </div>

    <!-- 商品列表 -->
    <div class="products-grid">
      <el-row :gutter="20" v-loading="loading">
        <el-col :span="6" v-for="product in products" :key="product.id">
          <el-card :body-style="{ padding: '0px' }" class="product-card">
            <img :src="product.image || 'https://via.placeholder.com/200x200?text=No+Image'" class="product-image" @click="viewDetail(product.id)" />
            <div style="padding: 14px;">
              <h3 class="product-title" @click="viewDetail(product.id)">{{ product.name }}</h3>
              <div class="product-price">¥{{ product.price }}</div>
              <div class="product-sales">销量: {{ product.sales || 0 }}</div>
              <div class="product-shop">店铺: {{ getShopName(product.shopId) }}</div>
              <div class="product-actions">
                <el-button size="small" type="primary" @click="addProductToCart(product.id)">加入购物车</el-button>
                <el-button size="small" @click="toggleFavorite(product.id)">
                  <el-icon><Star /></el-icon>
                </el-button>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && products.length === 0" class="empty-state">
      <el-empty description="暂无商品数据" />
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
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getShopList, getProductList, addToCart as apiAddToCart } from '@/api'

const searchKeyword = ref('')
const sortBy = ref('price_asc')
const currentPage = ref(1)
const pageSize = ref(12)
const total = ref(0)
const loading = ref(false)

const activeShop = ref('all')
const shops = ref([])
const products = ref([])

const handleSearch = () => {
  currentPage.value = 1
  loadProducts()
}

const resetSearch = () => {
  searchKeyword.value = ''
  sortBy.value = 'price_asc'
  activeShop.value = 'all'
  handleSearch()
}

const handleShopSelect = (shopId) => {
  activeShop.value = shopId
  currentPage.value = 1
  loadProducts()
}

const viewDetail = (productId) => {
  console.log('查看商品详情:', productId)
  // TODO: 跳转到商品详情页
}

const addProductToCart = async (productId) => {
  try {
    const userInfo = JSON.parse(localStorage.getItem('userInfo'))
    if (!userInfo) {
      ElMessage.warning('请先登录')
      return
    }
    
    // 查找商品信息
    const product = products.value.find(p => p.id === productId)
    if (!product) {
      ElMessage.error('商品信息不存在')
      return
    }
    
    // 调用API加入购物车
    const response = await apiAddToCart({
      userId: userInfo.id,
      productId: product.id,
      productName: product.name,
      price: product.price,
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

const toggleFavorite = (productId) => {
          console.log('切换收藏状态:', productId)
          // TODO: 收藏/取消收藏逻辑
        }

// 根据shopId获取店铺名称
const getShopName = (shopId) => {
  if (!shopId) return ''
  const shop = shops.value.find(s => s.id === shopId)
  return shop ? shop.shopName : '未知店铺'
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadProducts()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadProducts()
}

const loadShops = async () => {
  try {
    const response = await getShopList()
    if (response.code === 200) {
      shops.value = response.data || []
    }
  } catch (error) {
    console.error('获取店铺列表失败:', error)
    ElMessage.error('获取店铺列表失败')
  }
}

const loadProducts = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchKeyword.value,
      sort: sortBy.value
    }
    
    // 如果选择了特定店铺，添加店铺ID参数
    if (activeShop.value !== 'all') {
      params.shopId = parseInt(activeShop.value)
    }

    const response = await getProductList(params)
            if (response.code === 200) {
              // 商品API返回的是直接的数组，不是分页格式
              let productList = response.data || []
              
              // 根据选择的店铺筛选商品
              if (activeShop.value !== 'all') {
                productList = productList.filter(p => p.shopId === parseInt(activeShop.value))
              }
              
              // 根据搜索关键词筛选商品
              if (searchKeyword.value) {
                productList = productList.filter(p => 
                  p.name && p.name.includes(searchKeyword.value)
                )
              }
              
              products.value = productList
              total.value = products.value.length || 0
            }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
    products.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await loadShops()
  await loadProducts()
})
</script>

<style scoped>
.products-container {
  padding: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
  margin-bottom: 20px;
}

/* 店铺导航栏样式 */
.shop-nav {
  margin-bottom: 30px;
  background-color: #fafafa;
  border-radius: 8px;
  overflow-x: auto;
}

.shop-menu {
  border: none;
  background-color: transparent;
}

.shop-menu .el-menu-item {
  height: 50px;
  line-height: 50px;
  padding: 0 20px;
  margin: 5px;
  border-radius: 6px;
  transition: all 0.3s;
}

.shop-menu .el-menu-item:hover {
  background-color: #e6f7ff !important;
  color: #1890ff !important;
}

.shop-menu .el-menu-item.is-active {
  background-color: #1890ff !important;
  color: #fff !important;
}

.search-section {
  margin-bottom: 30px;
  padding: 20px;
  background-color: #f5f5f5;
  border-radius: 8px;
}

.products-grid {
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

.product-sales {
  font-size: 14px;
  color: #666;
  margin-bottom: 5px;
}

.product-shop {
  font-size: 14px;
  color: #888;
  margin-bottom: 10px;
}

.product-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.empty-state {
  text-align: center;
  margin: 100px 0;
}

.pagination {
  text-align: center;
  margin-top: 30px;
}
</style>