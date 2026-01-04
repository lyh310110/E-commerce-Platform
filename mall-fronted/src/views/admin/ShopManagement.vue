<template>
  <div class="shop-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>店铺管理</span>
        </div>
      </template>
      
      <el-form :inline="true" :model="searchForm" class="search-form">
        <el-form-item label="店铺名称">
          <el-input v-model="searchForm.shopName" placeholder="请输入店铺名称" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option
              v-for="option in getShopStatusOptions()"
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

      <el-table :data="shopList" border stripe v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="shopName" label="店铺名称" width="200" />
        <el-table-column prop="userId" label="用户ID" width="120" />
        <el-table-column prop="contactPhone" label="联系电话" width="150" />
        <el-table-column prop="shopDesc" label="店铺描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getShopStatusType(row.status)">
              {{ getShopStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button
              v-if="canApproveShop(row.status)"
              type="success"
              size="small"
              @click="handleApprove(row)"
            >
              通过
            </el-button>
            <el-button
              v-if="canRejectShop(row.status)"
              type="danger"
              size="small"
              @click="handleReject(row)"
            >
              拒绝
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

    <el-dialog v-model="dialogVisible" title="店铺详情" width="600px">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="店铺ID">{{ currentShop.id }}</el-descriptions-item>
        <el-descriptions-item label="店铺名称">{{ currentShop.shopName }}</el-descriptions-item>
        <el-descriptions-item label="用户ID">{{ currentShop.userId }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{ currentShop.contactPhone }}</el-descriptions-item>
        <el-descriptions-item label="状态" :span="2">
          <el-tag :type="getShopStatusType(currentShop.status)">
            {{ getShopStatusText(currentShop.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="申请时间" :span="2">{{ currentShop.createTime }}</el-descriptions-item>
        <el-descriptions-item label="店铺描述" :span="2">{{ currentShop.shopDesc || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { updateShop, getShopList } from '@/api'
import { UserConstant } from '@/constants'
import { getShopStatusText, getShopStatusType, getShopStatusOptions, canApproveShop, canRejectShop } from '@/utils/constantHelper'

const loading = ref(false)
const shopList = ref([])
const dialogVisible = ref(false)
const currentShop = ref({})

const searchForm = reactive({
  shopName: '',
  status: null
})

const pagination = reactive({
  pageNum: 1,
  pageSize: 10,
  total: 0
})

onMounted(() => {
  loadShopList()
})

const loadShopList = async () => {
  loading.value = true
  try {
    const params = {}
    if (searchForm.shopName) {
      params.shopName = searchForm.shopName
    }
    if (searchForm.status !== null) {
      params.status = searchForm.status
    }
    
    const res = await getShopList(params)
    if (res.code === 200) {
      shopList.value = res.data || []
      pagination.total = shopList.value.length
    }
  } catch (error) {
    console.error('加载店铺列表失败:', error)
    ElMessage.error('加载店铺列表失败')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.pageNum = 1
  loadShopList()
}

const handleReset = () => {
  searchForm.shopName = ''
  searchForm.status = null
  pagination.pageNum = 1
  loadShopList()
}

const handleApprove = async (row) => {
  try {
    await ElMessageBox.confirm('确定要通过该店铺的审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    console.log('正在更新店铺状态，店铺ID:', row.id, '新状态:', UserConstant.SHOP_STATUS.APPROVED)
    
    const res = await updateShop({
      id: row.id,
      status: UserConstant.SHOP_STATUS.APPROVED
    })
    
    console.log('更新店铺响应:', res)
    
    if (res.code === 200) {
      ElMessage.success('审核通过')
      const index = shopList.value.findIndex(item => item.id === row.id)
      if (index !== -1) {
        shopList.value[index].status = UserConstant.SHOP_STATUS.APPROVED
      }
    } else {
      ElMessage.error(res.msg || '审核失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('审核失败:', error)
      ElMessage.error(error.message || '审核失败')
    }
  }
}

const handleReject = async (row) => {
  try {
    await ElMessageBox.confirm('确定要拒绝该店铺的审核吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    console.log('正在更新店铺状态，店铺ID:', row.id, '新状态:', UserConstant.SHOP_STATUS.REJECTED)
    
    const res = await updateShop({
      id: row.id,
      status: UserConstant.SHOP_STATUS.REJECTED
    })
    
    console.log('更新店铺响应:', res)
    
    if (res.code === 200) {
      ElMessage.success('已拒绝')
      const index = shopList.value.findIndex(item => item.id === row.id)
      if (index !== -1) {
        shopList.value[index].status = UserConstant.SHOP_STATUS.REJECTED
      }
    } else {
      ElMessage.error(res.msg || '操作失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('操作失败:', error)
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const handleView = (row) => {
  currentShop.value = { ...row }
  dialogVisible.value = true
}

const handleSizeChange = (val) => {
  pagination.pageSize = val
  loadShopList()
}

const handleCurrentChange = (val) => {
  pagination.pageNum = val
  loadShopList()
}
</script>

<style scoped>
.shop-management {
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
