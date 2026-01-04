<template>
  <div class="product-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>商品管理</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="option in getProductStatusOptions()"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>

      <el-table :data="productList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" width="200" />
        <el-table-column prop="shopId" label="店铺ID" width="150" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="saleCount" label="销量" width="100" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getProductStatusType(row.status)">
              {{ getProductStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="canApproveProduct(row.status)"
              type="success"
              size="small"
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button
              v-if="canOffShelfProduct(row.status)"
              type="danger"
              size="small"
              @click="handleOffShelf(row)"
            >
              下架
            </el-button>
            <el-button
              type="primary"
              size="small"
              @click="handleView(row)"
            >
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="pagination.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>

    <el-dialog v-model="dialogVisible" title="商品详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="商品ID">{{ currentProduct.id }}</el-descriptions-item>
        <el-descriptions-item label="商品名称">{{ currentProduct.name }}</el-descriptions-item>
        <el-descriptions-item label="店铺ID">{{ currentProduct.shopId }}</el-descriptions-item>
        <el-descriptions-item label="价格">¥{{ currentProduct.price }}</el-descriptions-item>
        <el-descriptions-item label="库存">{{ currentProduct.stock }}</el-descriptions-item>
        <el-descriptions-item label="销量">{{ currentProduct.saleCount }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <el-tag :type="getProductStatusType(currentProduct.status)">
            {{ getProductStatusText(currentProduct.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="商品描述" :span="2">{{ currentProduct.description || '暂无描述' }}</el-descriptions-item>
        <el-descriptions-item label="创建时间" :span="2">{{ currentProduct.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getProductList, updateProduct, offShelfProduct } from '@/api'
import { UserConstant } from '@/constants'
import { getProductStatusText, getProductStatusType, getProductStatusOptions, canApproveProduct, canOffShelfProduct } from '@/utils/constantHelper'

const loading = ref(false)
const productList = ref([])
const dialogVisible = ref(false)
const currentProduct = ref({})

const searchForm = reactive({
  name: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  loadProductList()
})

const loadProductList = async () => {
  loading.value = true
  try {
    const res = await getProductList()
    if (res.code === 200) {
      let filteredData = res.data || []
      
      if (searchForm.name) {
        filteredData = filteredData.filter(item => 
          item.name.includes(searchForm.name)
        )
      }
      if (searchForm.status !== null) {
        filteredData = filteredData.filter(item => item.status === searchForm.status)
      }
      
      productList.value = filteredData
      pagination.total = filteredData.length
    }
  } catch (error) {
    console.error('加载商品列表失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadProductList()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.status = null
  pagination.pageNum = 1
  loadProductList()
}

const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm('确定要通过该商品的审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await updateProduct({ ...row, status: UserConstant.PRODUCT_STATUS.ON_SHELF })
    if (res.code === 200) {
      ElMessage.success('审核通过')
      loadProductList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审核失败:', error)
    }
  }
}

const handleOffShelf = async (row) => {
  try {
    await ElMessageBox.confirm('确定要下架该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const res = await offShelfProduct(row.id, row.shopId)
    if (res.code === 200) {
      ElMessage.success('下架成功')
      loadProductList()
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('下架失败:', error)
    }
  }
}

const handleView = (row) => {
  currentProduct.value = { ...row }
  dialogVisible.value = true
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadProductList()
}

const handleCurrentChange = (val) => {
  pagination.pageNum = val
  loadProductList()
}
</script>

<style scoped>
.product-management {
  padding: 20px;
}

.search-form {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
