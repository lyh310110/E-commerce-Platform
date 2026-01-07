<template>
  <div class="product-management-container">
    <h2>商品管理</h2>
    
    <el-card v-if="!loading && !shopInfo">
      <div class="no-shop-message">
        <el-empty description="您还没有创建店铺" />
        <el-button type="primary" style="margin-top: 20px;">立即创建店铺</el-button>
      </div>
    </el-card>
    
    <el-card v-else>
      <template #header>
        <div class="card-header">
          <span>商品列表</span>
          <el-button type="primary" @click="handleAddProduct">添加商品</el-button>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="商品名称">
          <el-input v-model="searchForm.name" placeholder="请输入商品名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="option in productStatusOptions"
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

      <el-table :data="filteredProductList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="name" label="商品名称" width="200" />
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" width="100" />
        <el-table-column prop="saleCount" label="销量" width="100" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'warning'">
              {{ getProductStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button 
              :type="row.status === 1 ? 'danger' : 'success'" 
              size="small"
              @click="handleStatusChange(row)"
            >
              {{ row.status === 1 ? '下架' : '上架' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-model:current-page="pagination.currentPage"
        v-model:page-size="pagination.pageSize"
        :total="filteredProductList.length"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        style="margin-top: 20px; justify-content: flex-end"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>



    <!-- 编辑商品对话框 -->
    <el-dialog
      v-model="editDialogVisible"
      title="编辑商品"
      width="500px"
    >
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="editForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input
            v-model="editForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number
            v-model="editForm.price"
            :min="0"
            :step="0.01"
            :precision="2"
            placeholder="请输入价格"
          />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number
            v-model="editForm.stock"
            :min="0"
            placeholder="请输入库存"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="editForm.status" placeholder="请选择状态">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="editDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveEdit">保存</el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 添加商品对话框 -->
    <el-dialog
      v-model="addDialogVisible"
      title="添加商品"
      width="500px"
    >
      <el-form :model="addForm" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="addForm.name" placeholder="请输入商品名称" />
        </el-form-item>
        <el-form-item label="商品描述">
          <el-input
            v-model="addForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入商品描述"
          />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number
            v-model="addForm.price"
            :min="0"
            :step="0.01"
            :precision="2"
            placeholder="请输入价格"
          />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number
            v-model="addForm.stock"
            :min="0"
            placeholder="请输入库存"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="addForm.status" placeholder="请选择状态">
            <el-option label="上架" :value="1" />
            <el-option label="下架" :value="0" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="addDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveAdd">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getShopInfo, getShopProducts, updateProduct, offShelfProduct, addProduct } from '@/api'

const loading = ref(false)
const userInfo = ref(null)
const shopInfo = ref(null)
const productList = ref([])



// 编辑对话框相关
const editDialogVisible = ref(false)
const editForm = reactive({
  id: null,
  name: '',
  description: '',
  price: 0,
  stock: 0,
  status: 1
})

// 添加商品对话框相关
const addDialogVisible = ref(false)
const addForm = reactive({
  name: '',
  description: '',
  price: 0,
  stock: 0,
  status: 1
})

const searchForm = reactive({
  name: '',
  status: null
})

const pagination = reactive({
  currentPage: 1,
  pageSize: 10
})

const productStatusOptions = [
  { label: '上架', value: 1 },
  { label: '下架', value: 0 }
]

const filteredProductList = computed(() => {
  let filtered = productList.value
  
  if (searchForm.name) {
    filtered = filtered.filter(item => item.name.includes(searchForm.name))
  }
  
  if (searchForm.status !== null) {
    filtered = filtered.filter(item => item.status === searchForm.status)
  }
  
  return filtered
})

onMounted(() => {
  loadUserData()
})

const loadUserData = () => {
  const userStr = localStorage.getItem('userInfo')
  console.log('从localStorage获取的用户信息:', userStr)
  if (userStr) {
    userInfo.value = JSON.parse(userStr)
    console.log('解析后的用户信息:', userInfo.value)
    loadShopInfo()
  } else {
    // 如果没有用户信息，手动模拟一个用户ID为6的卖家信息用于测试
    console.log('没有从localStorage获取到用户信息，使用模拟数据')
    userInfo.value = {
      id: 6,
      username: 'newseller001',
      nickname: '新测试卖家'
    }
    loadShopInfo()
  }
}

const loadShopInfo = async () => {
  loading.value = true
  try {
    console.log('调用getShopInfo，用户ID:', userInfo.value.id)
    const res = await getShopInfo(userInfo.value.id)
    console.log('getShopInfo返回结果:', res)
    if (res.code === 200) {
      shopInfo.value = res.data
      console.log('店铺信息:', shopInfo.value)
      loadProductData()
    } else {
      ElMessage.error(res.msg || '获取店铺信息失败')
    }
  } catch (error) {
    // 检查是否是店铺不存在的错误
    console.error('获取店铺信息异常:', error)
    if (error.message && error.message.includes('店铺不存在')) {
      shopInfo.value = null
      ElMessage.warning('您还没有创建店铺，请先创建店铺')
    } else {
      console.error('获取店铺信息失败:', error)
      ElMessage.error('获取店铺信息失败')
    }
  } finally {
    loading.value = false
  }
}

const loadProductData = async () => {
  loading.value = true
  try {
    const params = {
      shopId: shopInfo.value.id
    }
    console.log('调用getShopProducts，参数:', params)
    const res = await getShopProducts(params)
    console.log('getShopProducts返回结果:', res)
    if (res.code === 200) {
      productList.value = res.data || []
      console.log('商品列表数据:', productList.value)
    } else {
      ElMessage.error('获取商品列表失败')
    }
  } catch (error) {
    console.error('获取商品列表失败:', error)
    ElMessage.error('获取商品列表失败')
  } finally {
    loading.value = false
  }
}



const handleSearch = () => {
  pagination.currentPage = 1
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.status = null
  pagination.currentPage = 1
}

const getProductStatusText = (status) => {
  return status === 1 ? '上架' : '下架'
}

// 处理编辑按钮点击
const handleEdit = (row) => {
  // 填充表单数据
  Object.assign(editForm, row)
  // 显示对话框
  editDialogVisible.value = true
}

// 处理添加商品按钮点击
const handleAddProduct = () => {
  // 重置表单数据
  Object.assign(addForm, {
    name: '',
    description: '',
    price: 0,
    stock: 0,
    status: 1
  })
  // 显示对话框
  addDialogVisible.value = true
}

// 处理保存编辑
const handleSaveEdit = async () => {
  loading.value = true
  try {
    const res = await updateProduct(editForm)
    if (res.code === 200) {
      ElMessage.success('商品更新成功')
      editDialogVisible.value = false
      // 重新加载商品数据
      loadProductData()
    } else {
      ElMessage.error(res.msg || '商品更新失败')
    }
  } catch (error) {
    console.error('商品更新失败:', error)
    ElMessage.error('商品更新失败')
  } finally {
    loading.value = false
  }
}

// 处理保存添加商品
const handleSaveAdd = async () => {
  loading.value = true
  try {
    // 添加shopId到商品数据中
    const productData = {
      ...addForm,
      shopId: shopInfo.value.id
    }
    const res = await addProduct(productData)
    if (res.code === 200) {
      ElMessage.success('商品添加成功')
      addDialogVisible.value = false
      // 重新加载商品数据
      loadProductData()
    } else {
      ElMessage.error(res.msg || '商品添加失败')
    }
  } catch (error) {
    console.error('商品添加失败:', error)
    ElMessage.error('商品添加失败')
  } finally {
    loading.value = false
  }
}

// 处理商品上架/下架
const handleStatusChange = async (row) => {
  const action = row.status === 1 ? '下架' : '上架'
  try {
    await ElMessageBox.confirm(`确定要${action}该商品吗？`, '确认操作', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })

    loading.value = true
    if (action === '下架') {
      // 调用下架接口
      const res = await offShelfProduct(row.id, shopInfo.value.id)
      if (res.code === 200) {
        ElMessage.success('商品下架成功')
        // 更新本地数据
        row.status = 0
      } else {
        ElMessage.error(res.msg || '商品下架失败')
      }
    } else {
      // 上架操作，直接更新商品状态
      const updateData = {
        ...row,
        status: 1
      }
      const res = await updateProduct(updateData)
      if (res.code === 200) {
        ElMessage.success('商品上架成功')
        // 更新本地数据
        row.status = 1
      } else {
        ElMessage.error(res.msg || '商品上架失败')
      }
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error(`${action}商品失败:`, error)
      ElMessage.error(`${action}商品失败`)
    }
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
}

const handleCurrentChange = (val) => {
  pagination.currentPage = val
}


</script>

<style scoped>
.product-management-container {
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